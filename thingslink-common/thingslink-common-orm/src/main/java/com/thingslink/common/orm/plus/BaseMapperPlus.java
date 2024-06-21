package com.thingslink.common.orm.plus;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.thingslink.common.core.exception.BusinessException;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.exceptions.TooManyResultsException;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 基于BaseMapper的增强
 * @date : 2024/1/4 10:59
 */
public interface BaseMapperPlus<T> extends BaseMapper<T> {

    /**
     * 根据 ID 批量更新
     *
     * @param list 对象集合
     * @return 影响行数
     */
    int updateBatchByIds(@Param(Constants.COLL) List<T> list);

    /**
     * 根据 entity 条件，查询一条记录
     * <p>查询一条记录，例如 qw.last("limit 1") 限制取一条记录, 注意：多条数据只取第一条</p>
     *
     * @param queryWrapper 实体对象封装操作类（可以为 null）
     */
    default T selectOne(AbstractWrapper<T, ?, ?> queryWrapper) {
        queryWrapper.last("LIMIT 2 OFFSET 0");
        List<T> list = this.selectList(queryWrapper);
        int size = list.size();
        if (size == 1) {
            return list.get(0);
        }
        else if (size > 1) {
            throw new TooManyResultsException("Expected one result (or null) to be returned by selectOne(), but found 2");
        }
        return null;
    }

    /**
     * 根据 ID 查询
     */
    default <C> C selectVoById(Serializable id, Class<C> voClass) {
        T obj = this.selectById(id);
        if (ObjectUtil.isNull(obj)) {
            return null;
        }
        try {
            C c = voClass.getDeclaredConstructor().newInstance();
            BeanUtils.copyProperties(obj, c);
            return c;
        }
        catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new BusinessException(e.getMessage());
        }
    }

    /**
     * 根据 entity 条件,查询一条记录
     */
    default <C> C selectVoOne(AbstractWrapper<T, ?, ?> wrapper, Class<C> voClass) {
        T obj = this.selectOne(wrapper);
        if (ObjectUtil.isNull(obj)) {
            return null;
        }
        try {
            C c = voClass.getDeclaredConstructor().newInstance();
            BeanUtils.copyProperties(obj, c);
            return c;
        }
        catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new BusinessException(e.getMessage());
        }
    }

}
