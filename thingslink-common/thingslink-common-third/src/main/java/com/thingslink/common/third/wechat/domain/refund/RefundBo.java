package com.thingslink.common.third.wechat.domain.refund;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 退款参数，这里是微信侧的请求参数
 * @date : 2022/12/4 14:47
 */
@Data
@Accessors(chain = true)
public class RefundBo {
    /**
     * 子商户号
     */
    @JsonProperty("sub_mchid")
    private String subMchId;
    /**
     * 系统订单号
     */
    @JsonProperty("out_trade_no")
    private String orderNo;
    /**
     * 服务商退款单号
     */
    @JsonProperty("out_refund_no")
    private String refundNo;
    /**
     * 退款原因
     */
    @JsonProperty("reason")
    private String reason;
    /**
     * 退款给用户金额，单位分
     */
    @JsonProperty("refund")
    private Integer userRefund;
    /**
     * 原订单总金额，单位分
     */
    @JsonProperty("total")
    private Integer totalAmount;
}
