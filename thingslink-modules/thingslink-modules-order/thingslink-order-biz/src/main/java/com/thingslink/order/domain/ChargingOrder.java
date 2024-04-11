package com.thingslink.order.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.thingslink.common.orm.model.BaseEntity;
import com.thingslink.order.enums.PayType;
import com.thingslink.order.enums.ChargingStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 订单信息(Order)实体类
 *
 * @author wzkris
 * @since 2023-08-17 09:13:22
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ChargingOrder extends BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 260392207417900762L;

    @TableId
    @Schema(description = "充电id")
    private Long chargingId;

    @Schema(description = "系统交易号")
    private String orderNo;

    @Schema(description = "用户id，下单用户")
    private Long customerId;

    @Schema(description = "部门id，订单归属部门")
    private Long deptId;

    /**
     * {@link PayType}
     */
    @Schema(description = "支付方式")
    private String payType;

    /**
     * {@link ChargingStatus}
     */
    @Schema(description = "支付状态")
    private String chargingStatus;

    @Schema(description = "总金额")
    private Long totalAmount;

    @Schema(description = "支付金额")
    private Long payAmount;

    @Schema(description = "积分支付的金额")
    private Long pointAmount;

    @Schema(description = "退款金额")
    private Long refundAmount;

    @Schema(description = "付款时间")
    private LocalDateTime payTime;

    @Schema(description = "是否使用优惠券 0-否 1-是")
    private String isUseCoupon;

    public ChargingOrder(Long chargingId) {
        this.chargingId = chargingId;
    }
}
