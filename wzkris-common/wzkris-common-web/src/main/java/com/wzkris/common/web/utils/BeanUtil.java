package com.wzkris.common.web.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.cglib.beans.BeanCopier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Bean 工具类
 *
 * @author Michelle.Chung
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BeanUtil {

    private static final Map<BeanCopierKey, BeanCopier> BEAN_COPIER_CACHE = new ConcurrentHashMap<>();

    /**
     * 将 T 类型对象，转换为 desc 类型的对象并返回
     *
     * @param source 数据来源实体
     * @param desc   描述对象 转换后的对象
     * @return desc
     */
    public static <T, V> V convert(T source, Class<V> desc) {
        if (source == null) {
            return null;
        }
        V target = BeanUtils.instantiateClass(desc);
        return convert(source, target);
    }

    /**
     * 将 T 类型对象，按照配置的映射字段规则，给 desc 类型的对象赋值并返回 desc 对象
     *
     * @param source 数据来源实体
     * @param desc   转换后的对象
     * @return desc
     */
    public static <T, V> V convert(T source, V desc) {
        copyProperties(source, desc);
        return desc;
    }

    /**
     * 将 T 类型的集合，转换为 desc 类型的集合并返回
     *
     * @param sourceList 数据来源实体列表
     * @param desc       描述对象 转换后的对象
     * @return desc
     */
    public static <T, V> List<V> convert(List<T> sourceList, Class<V> desc) {
        if (CollectionUtils.isEmpty(sourceList)) {
            return Collections.emptyList();
        }
        List<V> targetList = new ArrayList<>(sourceList.size());
        BeanCopier beanCopier = null;
        for (T source : sourceList) {
            if (source == null) {
                continue;
            }
            V target = BeanUtils.instantiateClass(desc);
            if (beanCopier == null) {
                beanCopier = getBeanCopier(source.getClass(), desc);
            }
            beanCopier.copy(source, target, null);
            targetList.add(target);
        }
        return targetList;
    }

    private static <T, V> void copyProperties(T source, V target) {
        BeanCopier beanCopier = getBeanCopier(source.getClass(), target.getClass());
        beanCopier.copy(source, target, null);
    }

    private static BeanCopier getBeanCopier(Class<?> sourceClass, Class<?> targetClass) {
        BeanCopierKey key = new BeanCopierKey(sourceClass, targetClass);
        return BEAN_COPIER_CACHE.computeIfAbsent(key,
                beanCopierKey -> BeanCopier.create(beanCopierKey.sourceClass(), beanCopierKey.targetClass(), false));
    }

    private record BeanCopierKey(Class<?> sourceClass, Class<?> targetClass) {

    }

}
