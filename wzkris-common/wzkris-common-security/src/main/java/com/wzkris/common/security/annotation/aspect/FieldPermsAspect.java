package com.wzkris.common.security.annotation.aspect;

import com.wzkris.common.core.domain.CorePrincipal;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.security.annotation.CheckPerms;
import com.wzkris.common.security.annotation.FieldPerms;
import com.wzkris.common.security.annotation.enums.CheckMode;
import com.wzkris.common.security.annotation.enums.Rw;
import com.wzkris.common.security.oauth2.utils.PermissionUtil;
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
import org.springframework.security.access.AccessDeniedException;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Map;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 字段权限注解
 * @since 2024/12/26 16：20
 */
@Slf4j
@Aspect
@Order(1)
public class FieldPermsAspect {

    // 判断是否是基本类型或包装类（如 int, Integer, boolean, Boolean 等）
    public static boolean isPrimitiveOrWrapper(Class<?> clazz) {
        return clazz.isPrimitive()
                || clazz == Integer.class
                || clazz == Long.class
                || clazz == Short.class
                || clazz == Double.class
                || clazz == Float.class
                || clazz == Boolean.class
                || clazz == Character.class
                || clazz == Byte.class
                || clazz == String.class;
    }

    @Around("@annotation(fieldPerms) || @within(fieldPerms)")
    public Object around(ProceedingJoinPoint point, FieldPerms fieldPerms) throws Throwable {
        if (fieldPerms == null) {
            fieldPerms = AnnotationUtils.findAnnotation(
                    ((MethodSignature) point.getSignature()).getMethod(), FieldPerms.class);
        }

        boolean write = false, read = false;

        for (Rw perms : fieldPerms.rw()) {
            if (perms == Rw.WRITE) {
                write = true;
            }
            if (perms == Rw.READ) {
                read = true;
            }
        }

        if (write) {
            this.handleWritePerms(point.getArgs());
        }

        Object proceed = point.proceed();

        if (read) {
            this.handleReadPerms(proceed);
        }

        return proceed;
    }

    private void handleWritePerms(Object[] objs) throws IllegalAccessException {
        for (Object obj : objs) {
            if (obj == null) {
                continue;
            }

            if (isPrimitiveOrWrapper(obj.getClass())) { // 只处理方法返回对象类型数据
                continue;
            }

            this.doHandleWritePerms(obj);
        }
    }

    /**
     * 处理写权限
     *
     * @param obj 对象
     */
    private void doHandleWritePerms(Object obj) throws IllegalAccessException {
        if (obj == null) {
            return;
        }

        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            CheckPerms checkPerms = AnnotatedElementUtils.findMergedAnnotation(field, CheckPerms.class);
            if (checkPerms != null) {
                evaluatePerms(obj, field, checkPerms);
            } else {
                if (!isPrimitiveOrWrapper(field.getType())) { // 若是对象则递归其内部属性
                    this.doHandleWritePerms(FieldUtils.readField(obj, field.getName(), true));
                }
            }
        }
    }

    private void handleReadPerms(Object proceed) throws IllegalAccessException {
        if (isPrimitiveOrWrapper(proceed.getClass())) { // 只处理方法返回对象类型数据
            return;
        }

        this.doHandleReadPerms(proceed);
    }

    /**
     * 处理读权限
     *
     * @param obj 对象
     */
    private void doHandleReadPerms(Object obj) throws IllegalAccessException {
        if (obj == null) {
            return;
        }

        if (obj instanceof Collection<?> collection) {
            for (Object o : collection) {
                if (isPrimitiveOrWrapper(o.getClass())) {
                    break;
                }
                this.doHandleReadPerms(o);
            }
        } else if (obj instanceof Map<?, ?>) {
            log.warn("不支持返回值为Map类型的字段权限");
        } else { // 获取对象的类信息
            Class<?> clazz = obj.getClass();

            for (Field field : clazz.getDeclaredFields()) {
                // static 或 final 或 transient 跳过
                if (Modifier.isStatic(field.getModifiers())
                        || Modifier.isFinal(field.getModifiers())
                        || Modifier.isTransient(field.getModifiers())) {
                    continue;
                }
                CheckPerms checkPerms = AnnotatedElementUtils.findMergedAnnotation(field, CheckPerms.class);
                if (checkPerms != null) {
                    evaluatePerms(obj, field, checkPerms);
                } else {
                    // 为空则需要判断是否是基本类型
                    if (!isPrimitiveOrWrapper(field.getType())) {
                        this.doHandleReadPerms(FieldUtils.readField(obj, field.getName(), true));
                    }
                }
            }
        }
    }

    /**
     * 权限评估
     *
     * @param obj        对象
     * @param field      字段
     * @param checkPerms 权限注解
     */
    private void evaluatePerms(Object obj, Field field, CheckPerms checkPerms) throws IllegalAccessException {
        CorePrincipal principal = SecurityUtil.getPrincipal();

        if (!StringUtil.equals(principal.getType(), checkPerms.checkType().getValue())) {
            throw new AccessDeniedException(
                    "Principal needs checkType :" + checkPerms.checkType().getValue() + " , but have :" + principal.getType());
        }

        if (checkPerms.mode() == CheckMode.AND) {
            if (!PermissionUtil.hasPerms(checkPerms.value())) {
                FieldUtils.writeField(field, obj, null, true);
            }
        }

        if (checkPerms.mode() == CheckMode.OR) {
            if (!PermissionUtil.hasPermsOr(checkPerms.value())) {
                FieldUtils.writeField(field, obj, null, true);
            }
        }
    }

}
