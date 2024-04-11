package com.thingslink.order.mapper;

import com.thingslink.order.domain.Coupon;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 优惠券信息(Coupon)表数据库访问层
 *
 * @author wzkris
 * @since 2023-04-17 16:35:18
 */
@Repository
public interface CouponMapper {

    /**
     * 通过ID查询单条数据
     *
     * @param couponId 主键
     * @return 实例对象
     */
    Coupon getById(Long couponId);

    /**
     * 查询指定行数据
     *
     * @param coupon 查询条件
     * @return 对象列表
     */
    List<Coupon> list(Coupon coupon);

    /**
     * 新增数据
     *
     * @param coupon 实例对象
     * @return 影响行数
     */
    int insert(Coupon coupon);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<Coupon> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<Coupon> entities);

    /**
     * 修改数据
     *
     * @param coupon 实例对象
     * @return 影响行数
     */
    int updateById(Coupon coupon);


    /**
     * 批量修改数据
     *
     * @param entities 实例对象
     * @return 影响行数
     */
    int updateBatchById(@Param("entities") List<Coupon> entities);

    /**
     * 通过主键删除数据
     *
     * @param couponId 主键
     * @return 影响行数
     */
    int deleteById(Long couponId);

}
