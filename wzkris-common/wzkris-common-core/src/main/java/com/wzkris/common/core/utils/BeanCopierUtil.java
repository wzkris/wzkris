package com.wzkris.common.core.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 基于 cglib BeanCopier 的 Bean 拷贝工具
 * <p>通过缓存 BeanCopier 实例避免重复生成字节码，拷贝性能远优于纯反射方案（BeanUtils.copyProperties）</p>
 *
 * @author wzkris
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BeanCopierUtil {

    private static final Map<Key, BeanCopier> CACHE = new ConcurrentHashMap<>();

    /**
     * 将源对象拷贝到目标类型的新实例
     *
     * @param source      源对象，为 null 时返回 null
     * @param targetClass 目标类型
     */
    public static <S, T> T copy(S source, Class<T> targetClass) {
        if (source == null) {
            return null;
        }
        T target = BeanUtils.instantiateClass(targetClass);
        return copy(source, target);
    }

    /**
     * 将源对象属性拷贝到目标对象
     *
     * @param source 源对象
     * @param target 目标对象
     */
    public static <S, T> T copy(S source, T target) {
        obtain(source.getClass(), target.getClass()).copy(source, target, null);
        return target;
    }

    /**
     * 将源列表批量拷贝为目标类型列表
     *
     * @param sources     源列表，为空时返回空列表
     * @param targetClass 目标类型
     */
    public static <S, T> List<T> copyList(List<S> sources, Class<T> targetClass) {
        if (CollectionUtils.isEmpty(sources)) {
            return new ArrayList<>();
        }
        List<T> targets = new ArrayList<>(sources.size());
        BeanCopier copier = null;
        for (S source : sources) {
            if (source == null) {
                continue;
            }
            T target = BeanUtils.instantiateClass(targetClass);
            if (copier == null) {
                copier = obtain(source.getClass(), targetClass);
            }
            copier.copy(source, target, null);
            targets.add(target);
        }
        return targets;
    }

    private static BeanCopier obtain(Class<?> sourceClass, Class<?> targetClass) {
        return CACHE.computeIfAbsent(
                new Key(sourceClass, targetClass),
                k -> BeanCopier.create(k.sourceClass(), k.targetClass(), false)
        );
    }

    private record Key(Class<?> sourceClass, Class<?> targetClass) {
    }

}
