package com.wzkris.common.orm.plus;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.function.Function;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 基于 IService 的增强
 * @date : 2025/07/15
 */
public interface IServicePlus<T> extends IService<T> {

    /**
     * 插入记录后，通过 function 从实体中提取指定字段
     * <p>典型场景：插入后获取自增回填的主键 ID</p>
     *
     * @param entity 实体
     * @param func   插入后从实体中提取字段的函数
     * @param <R>    返回字段类型
     * @return 函数返回的值，插入失败返回 null
     */
    <R> R saveAndGet(T entity, Function<T, R> func);

    /**
     * 根据 ID 更新后，通过 function 从实体中提取指定字段
     *
     * @param entity 实体（必须包含 ID）
     * @param func   更新后从实体中提取字段的函数
     * @param <R>    返回字段类型
     * @return 函数返回的值，更新失败返回 null
     */
    <R> R updateByIdAndGet(T entity, Function<T, R> func);

    /**
     * 查询一条记录并提取指定字段
     *
     * @param wrapper 查询条件
     * @param func    字段提取函数
     * @param <R>     返回字段类型
     * @return 字段值，未查到返回 null
     */
    <R> R getOneField(Wrapper<T> wrapper, Function<T, R> func);

    /**
     * 根据单个字段值查询单条记录
     *
     * @param field 字段引用（如 Entity::getName）
     * @param value 字段值
     * @return 实体，未找到返回 null
     */
    T getOneByField(SFunction<T, ?> field, Object value);

    /**
     * 根据单个字段值查询单条记录，并提取指定字段
     *
     * @param field 查询字段引用
     * @param value 字段值
     * @param func  提取字段的函数
     * @param <R>   返回字段类型
     * @return 提取的字段值，未找到返回 null
     */
    <R> R getOneFieldByField(SFunction<T, ?> field, Object value, Function<T, R> func);

}
