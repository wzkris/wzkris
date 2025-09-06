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
            bucket.set(null, Duration.ofSeconds(15));// null值缓存15s
        }
        return proceedResult;
    }

    @Around("@annotation(globalCache)")
    public Object around(ProceedingJoinPoint point, GlobalCache globalCache) throws Throwable {
        // 拼接key并获取数据
        String globalKey = globalCache.keyPrefix();
        if (StringUtils.isNotBlank(globalCache.key())) {
            String key = spel.parseExpression(globalCache.key()).getValue(this.createContext(), String.class);
            globalKey = globalKey + ":" + key;
        }

        RBucket<Object> bucket = redissonClient.getBucket(globalKey);
        if (bucket.isExists()) {
            return bucket.get();
        }

        // 数据不存在需要执行方法并回写缓存
        Object o;
        if (globalCache.sync()) {
            o = DistLockTemplate.lockAndExecute(globalKey, 1_500, (ThrowableSupplier<Object, Throwable>) () -> {
                if (bucket.isExists()) {
                    return bucket.get();
                }
                return proceedAndRewrite(point, globalCache.ttl(), bucket);
            });
        } else {
            o = proceedAndRewrite(point, globalCache.ttl(), bucket);
        }

        return o;
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
