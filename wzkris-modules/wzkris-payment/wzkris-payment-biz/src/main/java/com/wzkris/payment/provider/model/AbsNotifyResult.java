package com.wzkris.payment.provider.model;

import com.wzkris.payment.enums.notify.NotifyTypeEnum;
import lombok.Getter;
import lombok.Setter;

/**
 * 渠道回调解析+验签结果基类（sealed）。
 *
 * <p>PAY/REFUND 各有专属子类型携带类型字段；{@link #notifyType()} 与 {@link #outBusinessNo()}
 * 由子类型提供，编排模板不再需要嗅探式钩子区分回调类型。
 *
 * @author wzkris
 */
@Getter
@Setter
public abstract sealed class AbsNotifyResult
        permits PayNotifyResult, RefundNotifyResult {

    /**
     * 渠道侧号（transaction_id / refund_id / trade_no，存档用）
     */
    private String channelNo;

    /**
     * 解析/处理错误信息（退款失败原因等）
     */
    private String errorMsg;

    /**
     * 原始报文
     */
    private String rawBody;

    /**
     * 回调类型，由子类型决定
     */
    public abstract NotifyTypeEnum notifyType();

    /**
     * 幂等业务号（PAY=order_no / REFUND=refund_no），由子类型提供
     */
    public abstract String outBusinessNo();

}
