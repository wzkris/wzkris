package com.thingslink.order.mapper;

import com.thingslink.common.orm.annotation.DeptScope;
import com.thingslink.order.domain.ChargingOrder;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 订单信息(ChargingOrder)表数据库访问层
 *
 * @author wzkris
 * @since 2023-08-17 09:19:08
 */
@Repository
public interface ChargingOrderMapper {

    /**
     * 通过ID查询单条数据
     *
     * @param chargingId 主键
     * @return 实例对象
     */
    ChargingOrder getById(Long chargingId);

    /**
     * 查询指定行数据
     *
     * @param order 查询条件
     * @return 对象列表
     */
    @DeptScope
    List<ChargingOrder> list(ChargingOrder order);

    /**
     * 新增数据
     *
     * @param order 实例对象
     * @return 影响行数
     */
    int insert(ChargingOrder order);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<ChargingOrder> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<ChargingOrder> entities);

    /**
     * 修改数据
     *
     * @param order 实例对象
     * @return 影响行数
     */
    int updateById(ChargingOrder order);

    /**
     * 批量修改数据
     *
     * @param entities 实例对象
     * @return 影响行数
     */
    int updateBatchById(@Param("entities") List<ChargingOrder> entities);

    /**
     * 通过主键删除数据
     *
     * @param chargingId 主键
     * @return 影响行数
     */
    int deleteById(Long chargingId);

}
