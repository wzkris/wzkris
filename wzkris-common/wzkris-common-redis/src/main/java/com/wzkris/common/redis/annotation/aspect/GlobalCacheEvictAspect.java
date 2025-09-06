package com.wzkris.common.redis.annotation.aspect;

import com.wzkris.common.core.utils.SpringUtil;
import com.wzkris.common.redis.annotation.GlobalCacheEvict;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

@Slf4j
@Aspect
public class GlobalCacheEvictAspect {

    private static volatile StandardEvaluationContext context;

    private final ExpressionParser spel = new SpelExpressionParser();

    @Autowired
    private RedissonClient redissonClient;

    @After("@annotation(globalCacheEvict)")
    public void after(JoinPoint point, GlobalCacheEvict globalCacheEvict) {
        // 拼接key并获取数据
        String globalKey = globalCacheEvict.keyPrefix();
        if (StringUtils.isNotBlank(globalCacheEvict.key())) {
            String key = spel.parseExpression(globalCacheEvict.key()).getValue(this.createContext(), String.class);
            globalKey = globalKey + ":" + key;
        }

        redissonClient.getBucket(globalKey).delete();
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
