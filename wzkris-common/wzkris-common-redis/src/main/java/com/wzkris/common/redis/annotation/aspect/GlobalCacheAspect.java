package com.wzkris.common.redis.annotation.aspect;

import com.wzkris.common.core.function.ThrowableSupplier;
import com.wzkris.common.core.utils.SpringUtil;
import com.wzkris.common.redis.annotation.GlobalCache;
import com.wzkris.common.redis.util.DistLockTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private RedissonClient redissonClient;

    /**
     * 执行原方法并回写缓存
     */
    private static Object proceedAndRewrite(ProceedingJoinPoint point, long ttl, RBucket<Object> bucket) throws Throwable {
        Object proceedResult = point.proceed();
        // 回写缓存
        if (proceedResult != null) {
            if (ttl > 0) {
                bucket.set(proceedResult, Duration.ofMillis(ttl));
            } else {
                bucket.set(proceedResult);
            }
        } else {
            // null值缓存15s，避免缓存穿透
            bucket.set(null, Duration.ofSeconds(15));
        }
        return proceedResult;
    }

    @Around("@annotation(globalCache)")
    public Object around(ProceedingJoinPoint point, GlobalCache globalCache) throws Throwable {
        String methodName = point.getSignature().getName();

        try {
            // 拼接key并获取数据
            String globalKey = globalCache.keyPrefix();
            if (StringUtils.isNotBlank(globalCache.key())) {
                String key = evaluateExpression(globalCache.key());
                globalKey = globalKey + ":" + key;
            }

            RBucket<Object> bucket = redissonClient.getBucket(globalKey);
            if (bucket.isExists()) {
                return bucket.get();
            }

            // 数据不存在需要执行方法并回写缓存
            Object result;
            if (globalCache.sync()) {
                result = DistLockTemplate.lockAndExecute(globalKey, 1_500, (ThrowableSupplier<Object, Throwable>) () -> {
                    if (bucket.isExists()) {
                        return bucket.get();
                    }
                    return proceedAndRewrite(point, globalCache.ttl(), bucket);
                });
            } else {
                result = proceedAndRewrite(point, globalCache.ttl(), bucket);
            }

            return result;
        } catch (Exception e) {
            log.error("缓存切面执行异常，方法: {}", methodName, e);
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
