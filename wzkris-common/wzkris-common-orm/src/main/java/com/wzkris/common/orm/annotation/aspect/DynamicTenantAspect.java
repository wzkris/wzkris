package com.wzkris.common.orm.annotation.aspect;


import com.wzkris.common.core.exception.BusinessException;
import com.wzkris.common.core.utils.SpringUtil;
import com.wzkris.common.orm.annotation.DynamicTenant;
import com.wzkris.common.orm.utils.DynamicTenantUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.security.access.expression.ExpressionUtils;

import java.lang.reflect.Method;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 动态租户权限切面
 * @date : 2024/04/13 16:21
 */
@Aspect
public class DynamicTenantAspect {
    private static final Logger log = LoggerFactory.getLogger(DynamicTenantAspect.class);
    // spel标准解析上下文
    private volatile static StandardEvaluationContext context;
    // spel解析器
    private final ExpressionParser spel = new SpelExpressionParser();
    // 方法参数名解析器
    private final DefaultParameterNameDiscoverer nameDiscoverer = new DefaultParameterNameDiscoverer();

    /**
     * 加入@DynamicTenant注解的方法执行前设置动态租户，执行后清除
     */
    @Around("@annotation(dynamicTenant)")
    public Object around(ProceedingJoinPoint point, DynamicTenant dynamicTenant) throws Throwable {
        String value = dynamicTenant.enableIgnore();
        DynamicTenant.ParseType parseType = dynamicTenant.parseType();
        return this.processPoint(point, parseType, value);
    }

    private Object processPoint(ProceedingJoinPoint point, DynamicTenant.ParseType parseType, String value) throws Throwable {
        switch (parseType) {
            case BOOLEAN -> {
                // bool值则直接转换
                boolean isIgnore = Boolean.parseBoolean(value);
                if (isIgnore) {
                    return DynamicTenantUtil.ignoreWithThrowable(point::proceed);
                }
                return point.proceed();
            }
            case NUMBER -> {
                // 解析租户ID
                Long tenantId = Long.valueOf(value);
                return DynamicTenantUtil.switchtWithThrowable(tenantId, point::proceed);
            }
            case SPEL_BOOLEAN -> {
                // 解析spel表达式
                boolean ignore = ExpressionUtils.evaluateAsBoolean(spel.parseExpression(value), this.createContext());
                if (ignore) {
                    return DynamicTenantUtil.ignoreWithThrowable(point::proceed);
                }
                return point.proceed();
            }
            case SPEL_NUMBER -> {
                // 解析spel，拿到对应参数
                Long tenantId = this.parseSpel(this.getMethod(point), point.getArgs(), value, Long.class);
                return DynamicTenantUtil.switchtWithThrowable(tenantId, point::proceed);
            }
            default -> throw new BusinessException("动态租户参数异常, parseType=" + parseType);
        }
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

    /**
     * 获取方法，兼容接口
     */
    private Method getMethod(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        if (method.getDeclaringClass().isInterface()) {
            try {
                method = joinPoint.getTarget().getClass().getDeclaredMethod(
                        joinPoint.getSignature().getName(),
                        method.getParameterTypes());
            }
            catch (SecurityException | NoSuchMethodException e) {
                throw new BusinessException(e.getMessage());
            }
        }
        return method;
    }

    /**
     * 解析 spel 表达式
     *
     * @param method      方法
     * @param arguments   参数
     * @param spelExpress 表达式
     * @param clazz       返回结果的类型
     * @return 执行spel表达式后的结果
     */
    private <T> T parseSpel(Method method, Object[] arguments, String spelExpress, Class<T> clazz) {
        String[] params = nameDiscoverer.getParameterNames(method);
        EvaluationContext context = new StandardEvaluationContext();
        for (int len = 0; len < params.length; len++) {
            context.setVariable(params[len], arguments[len]);
        }
        Expression expression = spel.parseExpression(spelExpress);
        return expression.getValue(context, clazz);
    }
}
