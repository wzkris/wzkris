package com.thingslink.common.orm.annotation.aspect;


import com.thingslink.common.core.exception.BusinessException;
import com.thingslink.common.core.utils.StringUtil;
import com.thingslink.common.orm.annotation.DynamicTenant;
import com.thingslink.common.orm.utils.TenantUtil;
import com.thingslink.common.security.utils.SysUserUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 动态租户权限切面
 * @date : 2024/04/13 16:21
 */
@Aspect
public class DynamicTenantAspect {
    // spel解析器
    private final ExpressionParser spel = new SpelExpressionParser();
    // 方法参数名解析器
    private final DefaultParameterNameDiscoverer nameDiscoverer = new DefaultParameterNameDiscoverer();

    /**
     * 加入@DynamicTenant注解的方法执行前设置动态租户，执行后清除
     */
    @Around("@annotation(dynamicTenant)")
    public Object around(ProceedingJoinPoint point, DynamicTenant dynamicTenant) throws Throwable {
        Long tenantId;
        // 为空则走自身租户
        if (StringUtil.isBlank(dynamicTenant.value())) {
            tenantId = SysUserUtil.getTenantId();
        }
        else {
            // 否则解析spel，拿到对应参数
            tenantId = this.parseSpel(this.getMethod(point), point.getArgs(), dynamicTenant.value(), Long.class);
        }
        try {
            TenantUtil.setDynamic(tenantId);
            return point.proceed();
        }
        finally {
            TenantUtil.clearDynamic();
        }
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
