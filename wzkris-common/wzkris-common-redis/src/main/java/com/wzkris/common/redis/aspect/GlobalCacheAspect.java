package com.wzkris.common.redis.aspect;

import com.wzkris.common.core.function.ThrowableSupplier;
import com.wzkris.common.core.utils.SpringUtil;
import com.wzkris.common.redis.annotation.GlobalCache;
import com.wzkris.common.redis.util.DistLockTemplate;
import com.wzkris.common.redis.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 全局缓存切面
 *
 * @author wzkris
 */
@Slf4j
@Aspect
public class GlobalCacheAspect {

    private static volatile StandardEvaluationContext context;

    private final ExpressionParser spel = new SpelExpressionParser();

    private final ConcurrentHashMap<String, org.springframework.expression.Expression> EXPRESSION_CACHE = new ConcurrentHashMap<>();

    /**
     * 执行原方法并回写缓存
     */
    private static Object proceedAndRewrite(ProceedingJoinPoint point, long ttl, String key) throws Throwable {
        Object proceedResult = point.proceed();
        // 回写缓存
        if (proceedResult != null) {
            if (ttl > 0) {
                RedisUtil.setObj(key, proceedResult, Duration.ofMillis(ttl).toSeconds());
            } else {
                RedisUtil.setObj(key, proceedResult);
            }
        }
        return proceedResult;
    }

    @Around("@annotation(globalCache)")
    public Object around(ProceedingJoinPoint point, GlobalCache globalCache) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) point.getSignature();

        try {
            String globalKey = buildCacheKey(globalCache);

            if (globalKey == null) {
                return point.proceed();
            }

            Object cachedValue = RedisUtil.getObj(globalKey, methodSignature.getReturnType());

            if (cachedValue != null) {
                return cachedValue;
            }

            Object result;
            if (globalCache.sync()) {
                result = DistLockTemplate.lockAndExecute(globalKey, 1_500,
                        (ThrowableSupplier<Object, Throwable>) () -> {
                            Object doubleCheck = RedisUtil.getObj(globalKey, methodSignature.getReturnType());
                            if (doubleCheck != null) return doubleCheck;
                            return proceedAndRewrite(point, globalCache.ttl(), globalKey);
                        });
            } else {
                result = proceedAndRewrite(point, globalCache.ttl(), globalKey);
            }
            return result;
        } catch (Exception e) {
            log.error("缓存切面异常", e);
            throw e;
        }
    }

    private String buildCacheKey(GlobalCache globalCache) {
        String globalKey = globalCache.keyPrefix();
        if (StringUtils.isNotBlank(globalCache.key())) {
            String key = evaluateExpression(globalCache.key());
            globalKey = globalKey + ":" + key;
        }
        return globalKey;
    }

    /**
     * 评估SpEL表达式，使用缓存避免重复解析
     */
    private String evaluateExpression(String expression) {
        // 先从缓存中获取已解析的表达式
        org.springframework.expression.Expression cachedExpression = EXPRESSION_CACHE.get(expression);
        if (cachedExpression == null) {
            cachedExpression = spel.parseExpression(expression);
            EXPRESSION_CACHE.put(expression, cachedExpression);
        }
        return cachedExpression.getValue(this.createContext(), String.class);
    }

    private StandardEvaluationContext createContext() {
        if (context == null) {
            synchronized (StandardEvaluationContext.class) {
                if (context == null) {
                    context = new StandardEvaluationContext();
                    context.setBeanResolver(new BeanFactoryResolver(SpringUtil.getFactory()));
                }
            }
        }
        return context;
    }

}