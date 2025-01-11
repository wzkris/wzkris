package com.wzkris.common.orm.plus;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wzkris.common.core.exception.BusinessException;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
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
     * <p>查询一条记录，例如 qw.last("limit 1") 限制取一条记录, 注意：多条数据只取第一条</p>
     *
     * @param queryWrapper 实体对象封装操作类（可以为 null）
     */
    default T selectOne(AbstractWrapper<T, ?, ?> queryWrapper) {
        queryWrapper.last("LIMIT 1 OFFSET 0");
        List<T> list = this.selectList(queryWrapper);
        if (list.size() == 1) {
            return list.get(0);
        }
        return null;
    }

    default T selectOneForUpdate(AbstractWrapper<T, ?, ?> queryWrapper) {
        queryWrapper.last("LIMIT 1 OFFSET 0 FOR UPDATE");
        List<T> list = this.selectList(queryWrapper);
        if (list.size() == 1) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 根据 ID 查询
     */
    default <C> C selectById2VO(Serializable id, Class<C> voClass) {
        T obj = this.selectById(id);
        if (ObjectUtil.isNull(obj)) {
            return null;
        }
        try {
            C c = voClass.getDeclaredConstructor().newInstance();
            BeanUtils.copyProperties(obj, c);
            return c;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new BusinessException(e.getMessage());
        }
    }

    /**
     * 根据 entity 条件,查询一条记录
     */
    default <C> C selectOne2VO(AbstractWrapper<T, ?, ?> wrapper, Class<C> voClass) {
        T obj = this.selectOne(wrapper);
        if (ObjectUtil.isNull(obj)) {
            return null;
        }
        try {
            C c = voClass.getDeclaredConstructor().newInstance();
            BeanUtils.copyProperties(obj, c);
            return c;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new BusinessException(e.getMessage());
        }
    }

    /**
     * 基于反射实现，或许存在性能问题，谨慎使用
     */
    default <C> List<C> selectList2VO(AbstractWrapper<T, ?, ?> wrapper, Class<C> voClass) {
        List<T> list = this.selectList(wrapper);
        if (ObjectUtil.isEmpty(list)) {
            return new ArrayList<>();
        }

        return list.stream().map(obj -> {
            try {
                C c = voClass.getDeclaredConstructor().newInstance();
                BeanUtils.copyProperties(obj, c);
                return c;
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                throw new BusinessException(e.getMessage());
            }
        }).collect(Collectors.toList());
    }
}
