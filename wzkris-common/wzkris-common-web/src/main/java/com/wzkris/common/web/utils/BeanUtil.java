package com.wzkris.common.web.utils;

import com.wzkris.common.core.utils.JsonUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.util.Collections;
import java.util.List;

/**
 * Bean 工具类
 *
 * @author Michelle.Chung
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BeanUtil {

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
        BeanUtils.copyProperties(source, target);
        return target;
    }

    /**
     * 将 T 类型对象，按照配置的映射字段规则，给 desc 类型的对象赋值并返回 desc 对象
     *
     * @param source 数据来源实体
     * @param desc   转换后的对象
     * @return desc
     */
    public static <T, V> V convert(T source, V desc) {
        if (source == null || desc == null) {
            return desc;
        }
        BeanUtils.copyProperties(source, desc);
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
        if (sourceList == null) {
            return null;
        }
        if (sourceList.isEmpty()) {
            return Collections.emptyList();
        }

        return JsonUtil.toColl(JsonUtil.toBytes(sourceList), List.class, desc);
    }

}
