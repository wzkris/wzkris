package com.thingslink.common.third.wechat.domain.refund;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 退款资金信息
 * @date : 2022/12/4 15:07
 */
@Data
@Accessors(chain = true)
@JsonIgnoreProperties
public class RefundAmount {
    /**
     * 订单总金额，单位为分
     */
    private Integer total;
    /**
     * 退款金额
     */
    @JsonProperty("refund")
    private Integer refundAmount;
    /**
     * 用户支付金额
     */
    @JsonProperty("payer_total")
    private Integer userTotal;
    /**
     * 用户退款金额
     */
    @JsonProperty("payer_refund")
    private Integer userRefund;
    /**
     * 应结退款金额
     */
    @JsonProperty("settlement_refund")
    private Integer settlementRefund;
    /**
     * 应结订单金额
     */
    @JsonProperty("settlement_total")
    private Integer settlementTotal;
    /**
     * 优惠退款金额
     */
    @JsonProperty("discount_refund")
    private Integer discountRefund;
    /**
     * 货币种类
     */
    private String currency;
}
