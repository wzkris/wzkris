package com.wzkris.common.orm.plus;

import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 基于BaseMapper的增强
 * @date : 2024/1/4 10:59
 */
public interface BaseMapperPlus<T> extends BaseMapper<T> {

    /**
     * 加锁查询
     */
    T selectByIdForUpdate(Serializable id);

    /**
     * 根据 entity 条件，查询一条记录
     * <p>重写 BaseMapper.selectOne，统一附加 LIMIT 1，查不到返回 null</p>
     *
     * @param wrapper 实体对象封装操作类（可以为 null）
     */
    @Override
    default T selectOne(Wrapper<T> wrapper) {
        if (wrapper instanceof AbstractWrapper) {
            ((AbstractWrapper<?, ?, ?>) wrapper).last("LIMIT 1 OFFSET 0");
        }
        List<T> list = this.selectList(wrapper);
        if (list.size() == 1) {
            return list.getFirst();
        }
        return null;
    }

    default T selectOneForUpdate(Wrapper<T> wrapper) {
        if (wrapper instanceof AbstractWrapper) {
            ((AbstractWrapper<?, ?, ?>) wrapper).last("LIMIT 1 OFFSET 0 FOR UPDATE");
        }
        List<T> list = this.selectList(wrapper);
        if (list.size() == 1) {
            return list.getFirst();
        }
        return null;
    }

    /**
     * 提取指定字段
     *
     * @param wrapper 查询条件
     * @param <R>     返回字段的类型
     * @return 查找到的字段值，若未找到返回 null
     */
    default <R> R selectOneField(Wrapper<T> wrapper, Function<T, R> func) {
        T t = selectOne(wrapper);
        return t == null ? null : func.apply(t);
    }

    /**
     * 根据单个字段值查询单条记录
     *
     * @param field 字段引用（如 Entity::getName）
     * @param value 字段值
     * @return 实体，未找到返回 null
     */
    default T selectOneByField(SFunction<T, ?> field, Object value) {
        return this.selectOne(new LambdaQueryWrapper<T>().eq(field, value));
    }

    /**
     * 根据单个字段值查询单条记录，并提取指定字段
     *
     * @param field 查询字段引用
     * @param value 字段值
     * @param func  提取字段的函数
     * @param <R>   返回字段类型
     * @return 提取的字段值，未找到返回 null
     */
    default <R> R selectOneFieldByField(SFunction<T, ?> field, Object value, Function<T, R> func) {
        T t = this.selectOneByField(field, value);
        return t == null ? null : func.apply(t);
    }

    /**
     * 根据 ID 查询
     */
    default <C> C selectById2VO(Serializable id, Class<C> voClass) {
        T obj = this.selectById(id);
        if (Objects.isNull(obj)) return null;

        C c = getInstance(voClass);
        BeanUtils.copyProperties(obj, c);
        return c;
    }

    /**
     * 根据 entity 条件,查询一条记录
     */
    default <C> C selectOne2VO(AbstractWrapper<T, ?, ?> wrapper, Class<C> voClass) {
        T obj = this.selectOne(wrapper);
        if (Objects.isNull(obj)) return null;

        C c = getInstance(voClass);
        BeanUtils.copyProperties(obj, c);
        return c;
    }

    /**
     * 基于反射实现，或许存在性能问题，谨慎使用
     */
    default <C> List<C> selectList2VO(AbstractWrapper<T, ?, ?> wrapper, Class<C> voClass) {
        List<T> list = this.selectList(wrapper);
        if (CollectionUtils.isEmpty(list)) {
            return new ArrayList<>();
        }

        return list.stream()
                .map(obj -> {
                    C c = getInstance(voClass);
                    BeanUtils.copyProperties(obj, c);
                    return c;
                })
                .collect(Collectors.toList());
    }

    /**
     * 插入记录后，通过 function 从实体中提取指定字段
     * <p>典型场景：插入后获取自增回填的主键 ID</p>
     *
     * @param entity 实体
     * @param func   插入后从实体中提取字段的函数
     * @param <R>    返回字段类型
     * @return 函数返回的值，插入失败返回 null
     */
    default <R> R insertAndGet(T entity, Function<T, R> func) {
        return this.insert(entity) > 0 ? func.apply(entity) : null;
    }

    /**
     * 根据 ID 更新后，通过 function 从实体中提取指定字段
     *
     * @param entity 实体（必须包含 ID）
     * @param func   更新后从实体中提取字段的函数
     * @param <R>    返回字段类型
     * @return 函数返回的值，更新失败返回 null
     */
    default <R> R updateByIdAndGet(T entity, Function<T, R> func) {
        return this.updateById(entity) > 0 ? func.apply(entity) : null;
    }

    private static <C> C getInstance(Class<C> voClass, Class<?>... parameterTypes) {
        C c;
        try {
            c = ReflectionUtils.accessibleConstructor(voClass, parameterTypes).newInstance();
        } catch (InstantiationException
                 | IllegalAccessException
                 | InvocationTargetException
                 | NoSuchMethodException e) {
            throw new RuntimeException(e.getMessage());
        }
        return c;
    }

}
