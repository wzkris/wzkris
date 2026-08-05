package com.wzkris.payment.provider.model;

import com.wzkris.payment.enums.notify.NotifyTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

/**
 * 渠道回调解析+验签结果
 *
 * <p>notifyType=PAY 携带支付字段；notifyType=REFUND 携带退款字段。
 *
 * @author wzkris
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotifyParseResult {

    /** 验签是否通过 */
    private boolean verifySuccess;

    /** 回调类型 */
    private NotifyTypeEnum notifyType;

    /** 我方订单号（PAY：即 order_no） */
    private String outTradeNo;

    /** 我方退款号（REFUND：即 refund_no） */
    private String outRefundNo;

    /** 渠道侧号（transaction_id / refund_id / trade_no，存档用） */
    private String channelNo;

    // ---- PAY 上下文 ----

    /** 渠道侧是否支付成功 */
    private boolean paid;

    private BigDecimal amount;

    private OffsetDateTime payAt;

    // ---- REFUND 上下文 ----

    /** 渠道侧是否退款成功 */
    private boolean refundSuccess;

    private BigDecimal refundAmount;

    private OffsetDateTime refundAt;

    /** 解析/处理错误信息（退款失败原因等） */
    private String errorMsg;

    private String rawBody;
}
