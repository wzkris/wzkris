package com.wzkris.common.orm.aspect;

import com.wzkris.common.core.enums.AuthTypeEnum;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.orm.annotation.DataColumn;
import com.wzkris.common.orm.annotation.DataScope;
import com.wzkris.common.orm.utils.DataScopeUtil;
import com.wzkris.common.security.model.AdminLoginUser;
import com.wzkris.common.security.utils.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

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
        if (!SecurityUtil.isAuth(AuthTypeEnum.ADMIN)) {
            return;
        }

        try {
            // 为每个列注入参数
            for (DataColumn dataColumn : dataScope.value()) {
                String column = StringUtil.isBlank(dataColumn.alias())
                        ? dataColumn.column()
                        : dataColumn.alias() + StringUtil.DOT + dataColumn.column();

                AdminLoginUser loginUser = SecurityUtil.getLoginUser(AdminLoginUser.class);
                DataScopeUtil.putParameter(column, loginUser.getDeptScopes());
            }
        } catch (Exception e) {
            log.error("Failed to inject data scope parameters: {}", e.getMessage(), e);
        }
    }

}
