package com.wzkris.common.redis.annotation.aspect;

import com.wzkris.common.core.function.ThrowableSupplier;
import com.wzkris.common.core.utils.SpringUtil;
import com.wzkris.common.core.utils.StringUtil;
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
 * @date : 2025/08/29
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
    private static <T> T proceedAndRewrite(ProceedingJoinPoint point, long ttl, String key) throws Throwable {
        Object proceedResult = point.proceed();
        // 回写缓存
        if (proceedResult != null) {
            if (ttl > 0) {
                RedisUtil.setObj(key, proceedResult, Duration.ofMillis(ttl));
            } else {
                RedisUtil.setObj(key, proceedResult);
            }
        } else {
            // null值缓存15s，避免缓存穿透
            RedisUtil.setObj(key, null, Duration.ofSeconds(15));
        }
        return (T) proceedResult;
    }

    @Around("@annotation(globalCache)")
    public Object around(ProceedingJoinPoint point, GlobalCache globalCache) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) point.getSignature();

        try {
            // 拼接key并获取数据
            String globalKey = globalCache.keyPrefix();
            if (StringUtils.isNotBlank(globalCache.key())) {
                String key = evaluateExpression(globalCache.key());
                globalKey = globalKey + ":" + key;
            }

            // 创建final变量供lambda使用
            final String finalGlobalKey = globalKey;

            // 尝试从缓存获取数据
            Object cachedValue = RedisUtil.getObj(finalGlobalKey, methodSignature.getReturnType());
            if (cachedValue != null) {
                return cachedValue;
            }

            // 数据不存在需要执行方法并回写缓存
            Object result;
            if (globalCache.sync()) {
                result = DistLockTemplate.lockAndExecute(finalGlobalKey, 1_500, (ThrowableSupplier<Object, Throwable>) () -> {
                    // 双重检查缓存
                    Object doubleCheckValue = RedisUtil.getObj(finalGlobalKey, methodSignature.getReturnType());
                    if (doubleCheckValue != null) {
                        return doubleCheckValue;
                    }
                    return proceedAndRewrite(point, globalCache.ttl(), finalGlobalKey);
                });
            } else {
                result = proceedAndRewrite(point, globalCache.ttl(), finalGlobalKey);
            }

            return result;
        } catch (Exception e) {
            log.error("缓存切面执行异常，方法: {}", point.getTarget().getClass().getName() + StringUtil.DOT + methodSignature.getName(),
                    e);
            throw e;
        }
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
