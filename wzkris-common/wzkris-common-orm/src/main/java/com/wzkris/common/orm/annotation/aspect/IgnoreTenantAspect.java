package com.wzkris.common.orm.annotation.aspect;

import com.wzkris.common.core.utils.SpringUtil;
import com.wzkris.common.orm.annotation.IgnoreTenant;
import com.wzkris.common.orm.utils.DynamicTenantUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 忽略租户
 */
@Slf4j
@Aspect
public class IgnoreTenantAspect {

    private static volatile StandardEvaluationContext context;

    private final ExpressionParser spel = new SpelExpressionParser();

    @Around("@annotation(ignoreTenant) || @within(ignoreTenant)")
    public Object around(ProceedingJoinPoint point, IgnoreTenant ignoreTenant) throws Throwable {
        if (ignoreTenant == null) {
            ignoreTenant = AnnotationUtils.findAnnotation(
                    ((MethodSignature) point.getSignature()).getMethod(), IgnoreTenant.class);
        }

        if (ignoreTenant.value()) {
            return DynamicTenantUtil.ignoreWithThrowable(point::proceed);
        }
        String forceTenantId = ignoreTenant.forceTenantId();
        Long tenantId = spel.parseExpression(forceTenantId).getValue(this.createContext(), Long.class);
        return DynamicTenantUtil.switchtWithThrowable(tenantId, point::proceed);
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
