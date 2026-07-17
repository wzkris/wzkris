package com.wzkris.common.orm.plus;

import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.wzkris.common.core.utils.BeanCopierUtil;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.List;
import java.util.function.Function;

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
     * 根据单个字段值查询单条记录
     *
     * @param condition      条件字段
     * @param conditionValue 条件字段值
     * @return 实体，未找到返回 null
     */
    default T selectOneByObj(SFunction<T, ?> condition, Object conditionValue) {
        return this.selectOne(new LambdaQueryWrapper<T>().eq(condition, conditionValue));
    }

    /**
     * 根据单个字段值查询列表记录
     *
     * @param condition      条件字段
     * @param conditionValue 条件字段值
     * @return 实体，未找到返回 null
     */
    default List<T> selectListByObj(SFunction<T, ?> condition, Object conditionValue) {
        return this.selectList(new LambdaQueryWrapper<T>().eq(condition, conditionValue));
    }

    /**
     * 提取一条指定字段
     *
     * @param wrapper 查询条件
     * @param <R>     返回字段的类型
     * @return 查找到的字段值，若未找到返回 null
     */
    default <R> R selectObj(Wrapper<T> wrapper, Function<Object, R> mapper) {
        T t = selectOne(wrapper);
        return t == null ? null : mapper.apply(t);
    }

    /**
     * 根据单个字段值查询单条记录，并提取指定字段
     *
     * @param queryField     查询字段
     * @param condition      条件字段
     * @param conditionValue 条件字段值
     * @param <R>            返回字段类型
     * @return 提取的字段值，未找到返回 null
     */
    default <R> R selectObjByObj(SFunction<T, R> queryField, SFunction<T, ?> condition, Object conditionValue) {
        List<Object> objs = this.selectObjs(
                new LambdaQueryWrapper<T>().select(queryField).eq(condition, conditionValue).last("LIMIT 1 OFFSET 0"));
        if (CollectionUtils.isEmpty(objs)) {
            return null;
        }
        return (R) objs.getFirst();
    }

    /**
     * 根据 ID 查询并转换为 VO
     */
    default <C> C selectById2VO(Serializable id, Class<C> voClass) {
        return BeanCopierUtil.copy(this.selectById(id), voClass);
    }

    /**
     * 根据 entity 条件,查询一条记录并转换为 VO
     */
    default <C> C selectOne2VO(Wrapper<T> wrapper, Class<C> voClass) {
        return BeanCopierUtil.copy(this.selectOne(wrapper), voClass);
    }

    /**
     * 查询列表并转换为 VO
     */
    default <C> List<C> selectList2VO(Wrapper<T> wrapper, Class<C> voClass) {
        return BeanCopierUtil.copyList(this.selectList(wrapper), voClass);
    }

    /**
     * 插入记录后，通过 function 从实体中提取指定字段
     * <p>典型场景：插入后获取自增回填的主键 ID</p>
     *
     * @param entity 实体
     * @param mapper 插入后从实体中提取字段的函数
     * @param <R>    返回字段类型
     * @return 函数返回的值，插入失败返回 null
     */
    default <R> R insertAndGet(T entity, Function<T, R> mapper) {
        return this.insert(entity) > 0 ? mapper.apply(entity) : null;
    }

    /**
     * 根据 ID 更新后，通过 function 从实体中提取指定字段
     *
     * @param entity 实体（必须包含 ID）
     * @param mapper 更新后从实体中提取字段的函数
     * @param <R>    返回字段类型
     * @return 函数返回的值，更新失败返回 null
     */
    default <R> R updateByIdAndGet(T entity, Function<T, R> mapper) {
        return this.updateById(entity) > 0 ? mapper.apply(entity) : null;
    }

}
