package com.wzkris.common.orm.plus;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.io.Serializable;
import java.util.List;
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
    public <V> V getObj(Wrapper<T> wrapper, Function<? super Object, V> func) {
        return baseMapper.selectObj(wrapper, func);
    }

    @Override
    public T getOneByObj(SFunction<T, ?> field, Object value) {
        return baseMapper.selectOneByObj(field, value);
    }

    @Override
    public <R> R getObjByObj(SFunction<T, R> queryField, SFunction<T, ?> condition, Object value) {
        return baseMapper.selectObjByObj(queryField, condition, value);
    }

    @Override
    public <C> C getById2VO(Serializable id, Class<C> voClass) {
        return baseMapper.selectById2VO(id, voClass);
    }

    @Override
    public <C> C getOne2VO(Wrapper<T> wrapper, Class<C> voClass) {
        return baseMapper.selectOne2VO(wrapper, voClass);
    }

    @Override
    public <C> List<C> list2VO(Wrapper<T> wrapper, Class<C> voClass) {
        return baseMapper.selectList2VO(wrapper, voClass);
    }

}
