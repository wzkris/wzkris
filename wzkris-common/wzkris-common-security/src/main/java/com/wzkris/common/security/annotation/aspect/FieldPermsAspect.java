package com.wzkris.common.security.annotation.aspect;

import com.wzkris.common.core.model.CorePrincipal;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.security.annotation.CheckPerms;
import com.wzkris.common.security.annotation.FieldPerms;
import com.wzkris.common.security.annotation.enums.CheckMode;
import com.wzkris.common.security.annotation.enums.Rw;
import com.wzkris.common.security.utils.PermissionUtil;
import com.wzkris.common.security.utils.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 字段权限注解切面（优化版）
 * @since 2024/12/26 16：20
 */
@Slf4j
@Aspect
@Order(1)
public class FieldPermsAspect {

    @Around("@annotation(fieldPerms) || @within(fieldPerms)")
    public Object around(ProceedingJoinPoint point, FieldPerms fieldPerms) throws Throwable {
        fieldPerms = fieldPerms != null ? fieldPerms : getAnnotation(point);

        boolean hasWrite = hasPermission(fieldPerms, Rw.WRITE);
        boolean hasRead = hasPermission(fieldPerms, Rw.READ);

        if (hasWrite) processArgs(point.getArgs());
        Object result = point.proceed();
        if (hasRead) processResult(result);

        return result;
    }

    private FieldPerms getAnnotation(ProceedingJoinPoint point) {
        return AnnotationUtils.findAnnotation(
                ((MethodSignature) point.getSignature()).getMethod(), FieldPerms.class);
    }

    private boolean hasPermission(FieldPerms fieldPerms, Rw permission) {
        return Arrays.asList(fieldPerms.rw()).contains(permission);
    }

    private void processArgs(Object[] args) {
        Arrays.stream(args)
                .filter(arg -> arg != null && !isPrimitive(arg.getClass()))
                .forEach(this::processObject);
    }

    private void processResult(Object result) {
        if (result != null && !isPrimitive(result.getClass())) {
            processObject(result);
        }
    }

    private void processObject(Object obj) {
        if (obj == null) return;

        if (obj instanceof Collection) ((Collection<?>) obj).forEach(this::processObject);
        else if (obj instanceof Map) ((Map<?, ?>) obj).values().forEach(this::processObject);
        else processFields(obj);
    }

    private void processFields(Object obj) {
        for (Field field : obj.getClass().getDeclaredFields()) {
            if (isSkipField(field)) continue;

            CheckPerms checkPerms = AnnotatedElementUtils.findMergedAnnotation(field, CheckPerms.class);
            checkPermission(obj, field, checkPerms);
        }
    }

    private void checkPermission(Object obj, Field field, CheckPerms checkPerms) {
        try {
            CorePrincipal principal = SecurityUtil.getPrincipal();
            if (principal == null || !StringUtil.equals(principal.getType(), checkPerms.checkType().getValue())) {
                return;
            }

            boolean hasPerm = checkPerms.mode() == CheckMode.AND ?
                    PermissionUtil.hasPerms(checkPerms.value()) :
                    PermissionUtil.hasPermsOr(checkPerms.value());

            if (!hasPerm) {
                FieldUtils.writeField(field, obj, null, true);
            }
        } catch (IllegalAccessException e) {
            log.warn("字段权限反射失败", e);
        }
    }

    private boolean isPrimitive(Class<?> clazz) {
        return clazz.isPrimitive() || clazz == String.class ||
                Number.class.isAssignableFrom(clazz) || clazz == Boolean.class;
    }

    private boolean isSkipField(Field field) {
        int mod = field.getModifiers();
        return Modifier.isStatic(mod) || Modifier.isFinal(mod);
    }

}