package com.wzkris.common.orm.plus;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.function.Function;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 基于 ServiceImpl 的增强
 * @date : 2025/07/15
 */
public class ServiceImplPlus<M extends BaseMapperPlus<T>, T> extends ServiceImpl<M, T> implements IServicePlus<T> {

    @Override
    public <R> R saveAndGet(T entity, Function<T, R> func) {
        return baseMapper.insertAndGet(entity, func);
    }

    @Override
    public <R> R updateByIdAndGet(T entity, Function<T, R> func) {
        return baseMapper.updateByIdAndGet(entity, func);
    }

    @Override
    public <R> R getOneField(Wrapper<T> wrapper, Function<T, R> func) {
        return baseMapper.selectOneField(wrapper, func);
    }

    @Override
    public T getOneByField(SFunction<T, ?> field, Object value) {
        return baseMapper.selectOneByField(field, value);
    }

    @Override
    public <R> R getOneFieldByField(SFunction<T, ?> field, Object value, Function<T, R> func) {
        return baseMapper.selectOneFieldByField(field, value, func);
    }

}
