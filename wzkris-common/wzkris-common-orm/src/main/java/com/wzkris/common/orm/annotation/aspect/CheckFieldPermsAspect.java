package com.wzkris.common.orm.annotation.aspect;

import cn.hutool.core.util.ArrayUtil;
import com.wzkris.common.core.utils.ReflectUtil;
import com.wzkris.common.core.utils.SpringUtil;
import com.wzkris.common.orm.annotation.CheckFieldPerms;
import com.wzkris.common.orm.annotation.FieldPerms;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.security.access.expression.ExpressionUtils;

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
public class CheckFieldPermsAspect {

    private volatile static StandardEvaluationContext context;

    private final ExpressionParser spel = new SpelExpressionParser();

    // 判断是否是基本类型或包装类（如 int, Integer, boolean, Boolean 等）
    public static boolean isPrimitiveOrWrapper(Class<?> clazz) {
        return clazz.isPrimitive() ||
                clazz == Integer.class || clazz == Long.class || clazz == Short.class ||
                clazz == Double.class || clazz == Float.class || clazz == Boolean.class ||
                clazz == Character.class || clazz == Byte.class || clazz == String.class;
    }

    @Around("@annotation(checkFieldPerms) || @within(checkFieldPerms)")
    public Object around(ProceedingJoinPoint point, CheckFieldPerms checkFieldPerms) throws Throwable {
        if (checkFieldPerms == null) {
            checkFieldPerms = AnnotationUtils.findAnnotation(((MethodSignature) point.getSignature()).getMethod(), CheckFieldPerms.class);
        }

        CheckFieldPerms.Perms rw = checkFieldPerms.rw();
        String spel = checkFieldPerms.value();
        Class<?>[] groups = checkFieldPerms.groups();

        if (rw == CheckFieldPerms.Perms.WRITE) {
            this.handleWritePerms(point.getArgs(), spel, groups);
        }

        Object proceed = point.proceed();

        if (rw == CheckFieldPerms.Perms.READ) {
            this.handleReadPerms(proceed, spel, groups);
        }
        return proceed;
    }

    private void handleWritePerms(Object[] objs, String spel, Class<?>[] groups) {
        for (Object obj : objs) {
            if (obj == null) {
                continue;
            }

            if (isPrimitiveOrWrapper(obj.getClass())) { // 只处理方法返回对象类型数据
                continue;
            }

            this.handleWritePerms(obj, spel, groups);
        }
    }

    /**
     * 处理写权限
     *
     * @param obj    对象
     * @param spel   表达式
     * @param groups 分组
     */
    private void handleWritePerms(Object obj, String spel, Class<?>[] groups) {
        if (obj == null) {
            return;
        }

        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            FieldPerms fieldPerms = field.getAnnotation(FieldPerms.class);
            if (fieldPerms != null) {
                // 处理权限
                this.handleParameterPerms(obj, field, fieldPerms, spel, groups);
            }
            else {
                if (!isPrimitiveOrWrapper(field.getType())) { // 若是对象则递归其内部属性
                    this.handleWritePerms(ReflectUtil.getFieldValue(obj, field), spel, groups);
                }
            }
        }
    }

    private void handleReadPerms(Object proceed, String spel, Class<?>[] groups) {
        if (isPrimitiveOrWrapper(proceed.getClass())) { // 只处理方法返回对象类型数据
            return;
        }

        this.doHandleReadPerms(proceed, spel, groups);
    }

    /**
     * 处理读权限
     *
     * @param obj    对象
     * @param spel   表达式
     * @param groups 分组
     */
    private void doHandleReadPerms(Object obj, String spel, Class<?>[] groups) {
        if (obj == null) {
            return;
        }

        if (obj instanceof Collection<?> collection) {
            for (Object o : collection) {
                if (isPrimitiveOrWrapper(o.getClass())) {
                    break;
                }
                this.doHandleReadPerms(o, spel, groups);
            }
        }
        else if (obj instanceof Map<?, ?>) {
            log.warn("不支持返回值为Map类型的字段权限");
        }
        else {// 获取对象的类信息
            Class<?> clazz = obj.getClass();

            for (Field field : clazz.getDeclaredFields()) {
                // static 或 final 或 transient 跳过
                if (Modifier.isStatic(field.getModifiers())
                        || Modifier.isFinal(field.getModifiers())
                        || Modifier.isTransient(field.getModifiers())) {
                    continue;
                }
                FieldPerms fieldPerms = field.getAnnotation(FieldPerms.class);
                if (fieldPerms != null) {
                    // 处理权限
                    this.handleParameterPerms(obj, field, fieldPerms, spel, groups);
                }
                else {
                    // 为空则需要判断是否是基本类型
                    if (!isPrimitiveOrWrapper(field.getType())) {
                        this.doHandleReadPerms(ReflectUtil.getFieldValue(obj, field), spel, groups);
                    }
                }
            }
        }
    }

    /**
     * 处理参数权限
     */
    private void handleParameterPerms(Object obj, Field field, FieldPerms fieldPerms, String spel, Class<?>[] groups) {
        // 分组匹配
        if (!isMatchingGroups(fieldPerms.groups(), groups)) {
            return;
        }

        // 权限评估
        evaluatePerms(obj, field, spel);
    }

    // 检查字段权限分组是否匹配
    private boolean isMatchingGroups(Class<?>[] fieldGroups, Class<?>[] methodGroups) {
        if (fieldGroups.length == 0 && methodGroups.length == 0) {
            return true;
        }

        if (fieldGroups.length == 0 || methodGroups.length == 0) {
            return false;
        }

        for (Class<?> group : methodGroups) {
            if (ArrayUtil.contains(fieldGroups, group)) {
                return true;
            }
        }

        return false;
    }

    /**
     * @param field      字段
     * @param expression 权限表达式
     */
    private void evaluatePerms(Object arg, Field field, String expression) {
        if (!ExpressionUtils.evaluateAsBoolean(spel.parseExpression(expression), this.createContext())) {
            ReflectUtil.setFieldValue(arg, field, null);
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
}
