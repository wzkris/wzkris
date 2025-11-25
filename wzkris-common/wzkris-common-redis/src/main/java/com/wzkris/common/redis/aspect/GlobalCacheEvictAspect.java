package com.wzkris.common.redis.aspect;

import com.wzkris.common.core.utils.SpringUtil;
import com.wzkris.common.redis.annotation.GlobalCacheEvict;
import com.wzkris.common.redis.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Aspect
public class GlobalCacheEvictAspect {

    private static volatile StandardEvaluationContext context;

    private final ExpressionParser spel = new SpelExpressionParser();

    private final ConcurrentHashMap<String, org.springframework.expression.Expression> EXPRESSION_CACHE = new ConcurrentHashMap<>();

    @After("@annotation(globalCacheEvict)")
    public void after(JoinPoint point, GlobalCacheEvict globalCacheEvict) {
        String methodName = point.getSignature().getName();

        try {
            // 拼接key并获取数据
            String globalKey = globalCacheEvict.keyPrefix();
            if (StringUtils.isNotBlank(globalCacheEvict.key())) {
                String key = evaluateExpression(globalCacheEvict.key());
                globalKey = globalKey + ":" + key;
            }

            RedisUtil.delObj(globalKey);
        } catch (Exception e) {
            log.error("缓存驱逐切面执行异常，方法: {}", methodName, e);
        }
    }

    /**
     * 评估SpEL表达式，使用缓存避免重复解析
     */
    private String evaluateExpression(String expression) {
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
