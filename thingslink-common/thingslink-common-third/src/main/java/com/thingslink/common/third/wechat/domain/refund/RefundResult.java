package com.thingslink.common.third.wechat.domain.refund;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 退款信息
 * @date : 2022/12/4 14:58
 */
@Data
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RefundResult {
    /**
     * 微信支付退款单号
     */
    @JsonProperty("refund_id")
    private String outRefundNo;
    /**
     * 商户退款单号
     */
    @JsonProperty("out_refund_no")
    private String refundNo;
    /**
     * 微信支付订单号
     */
    @JsonProperty("transaction_id")
    private String outTradeNo;
    /**
     * 商户订单号
     */
    @JsonProperty("out_trade_no")
    private String orderNo;
    /**
     * 退款渠道  ORIGINAL：原路退款   BALANCE：退回到余额
     */
    @JsonProperty("channel")
    private String channel;
    /**
     * 退款入账账户
     * 1）退回银行卡：{银行名称}{卡类型}{卡尾号}
     * 2）退回支付用户零钱:支付用户零钱
     * 3）退还商户:商户基本账户商户结算银行账户
     * 4）退回支付用户零钱通:支付用户零钱通
     */
    @JsonProperty("user_received_account")
    private String userReceivedAccount;
    /**
     * 退款成功时间
     */
    @JsonProperty("success_time")
    private Date successTime;
    /**
     * 退款创建时间
     */
    @JsonProperty("create_at")
    private Date createAt;
    /**
     * 退款状态
     */
    @JsonAlias({"status", "refund_status"})
    private String status;
    /**
     * 资金账户
     * UNSETTLED : 未结算资金
     * AVAILABLE : 可用余额
     */
    @JsonProperty("funds_account")
    private String fundsAccount;
    @JsonProperty("amount")
    private RefundAmount refundAmount;

    /**
     * 是否退款成功
     */
    public boolean isSuccess() {
        return "SUCCESS".equals(this.status);
    }
}
