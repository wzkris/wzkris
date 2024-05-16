package com.thingslink.order.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.thingslink.order.enums.CouponStatus;
import com.thingslink.order.enums.CouponType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 优惠券信息(Coupon)实体类
 *
 * @author wzkris
 * @since 2023-04-17 16:35:18
 */
@Data
@Accessors(chain = true)
public class Coupon implements Serializable {
    @Serial
    private static final long serialVersionUID = 151394482015291584L;

    @TableId
    @Schema(description = "优惠券id")
    private Long couponId;

    @Schema(description = "标题")
    private String couponTitle;
    /**
     * {@link CouponStatus}
     */
    @Schema(description = "优惠券状态")
    private String couponStatus;
    /**
     * {@link CouponType}
     */
    @Schema(description = "优惠券类型")
    private String couponType;

    @Schema(description = "优惠券描述")
    private String couponDesc;

    @Schema(description = "门槛金额，0则为无门槛")
    private Integer limitAmount;

    @Schema(description = "优惠金额")
    private Integer discountAmount;

    @Schema(description = "使用开始时间")
    private LocalDateTime beginTime;

    @Schema(description = "使用结束时间")
    private LocalDateTime endTime;

    @Schema(description = "券总数")
    private Integer sumQuantity;

    @Schema(description = "券剩余数量")
    private Integer remainingQuantity;

    @Schema(description = "用户限制持有券数量")
    private Integer limitQuantity;
}
