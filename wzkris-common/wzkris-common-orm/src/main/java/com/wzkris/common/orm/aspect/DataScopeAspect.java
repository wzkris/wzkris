package com.wzkris.common.orm.aspect;

import com.wzkris.common.core.utils.SpringUtil;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.orm.annotation.DataColumn;
import com.wzkris.common.orm.annotation.DataScope;
import com.wzkris.common.orm.utils.DataScopeUtil;
import com.wzkris.common.security.utils.AdminUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.Order;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 数据权限切面
 * 自动注入数据权限参数
 *
 * @author : wzkris
 * @version : V1.0.0
 * @description : 数据权限切面
 * @date : 2025/01/XX
 */
@Slf4j
@Aspect
@Component
@Order(1)
public class DataScopeAspect {

    /**
     * SpEL 表达式解析器
     */
    private final ExpressionParser spelParser = new SpelExpressionParser();

    private static volatile StandardEvaluationContext context;

    /**
     * 表达式缓存，避免重复解析
     */
    private final ConcurrentHashMap<String, Expression> expressionCache = new ConcurrentHashMap<>();

    @Pointcut("@annotation(com.wzkris.common.orm.annotation.DataScope)")
    public void pointcutMethod() {
    }

    @Pointcut("@within(com.wzkris.common.orm.annotation.DataScope)")
    public void pointcutClass() {
    }

    @Around("pointcutMethod() || pointcutClass()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        // 优先使用方法上的注解，如果没有则使用类上的注解
        DataScope dataScope = AnnotatedElementUtils.findMergedAnnotation(method, DataScope.class);
        if (dataScope == null) {
            dataScope = AnnotatedElementUtils.findMergedAnnotation(method.getDeclaringClass(), DataScope.class);
        }

        if (dataScope != null) {
            try {
                // 注入参数
                injectParameters(dataScope);
                // 执行方法
                return joinPoint.proceed();
            } finally {
                // 清理参数
                DataScopeUtil.remove();
            }
        }

        return joinPoint.proceed();
    }

    /**
     * 注入数据权限参数
     */
    private void injectParameters(DataScope dataScope) {
        if (!AdminUtil.isLogin()) {
            return;
        }

        try {
            // 为每个列注入参数
            for (DataColumn dataColumn : dataScope.value()) {
                String column = StringUtil.isBlank(dataColumn.alias())
                        ? dataColumn.column()
                        : dataColumn.alias() + StringUtil.DOT + dataColumn.column();

                // 使用 SpEL 表达式获取对应的权限范围值
                Object scopeValue = getScopeValueBySpel(dataColumn.source());
                if (scopeValue != null) {
                    DataScopeUtil.putParameter(column, scopeValue);
                }
            }
        } catch (Exception e) {
            log.error("Failed to inject data scope parameters: {}", e.getMessage());
        }
    }

    /**
     * 创建 SpEL 评估上下文
     *
     * @return SpEL 上下文
     */
    private StandardEvaluationContext createSpelContext() {
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
     * 通过 SpEL 表达式获取权限范围值
     *
     * @param source SpEL 表达式，如 "au.getDeptScopes()"、"@au.getDeptScopes()"、"admin.deptScopes" 等
     * @return 权限范围值
     */
    private Object getScopeValueBySpel(String source) {
        try {
            // 从缓存获取或解析表达式
            Expression expression = expressionCache.computeIfAbsent(source, spelParser::parseExpression);

            // 执行表达式获取值
            return expression.getValue(createSpelContext());
        } catch (Exception e) {
            log.error("Failed to evaluate SpEL expression '{}': {}", source, e.getMessage());
            return null;
        }
    }

}

