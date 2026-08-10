package com.wzkris.payment.provider.model;

/**
 * 渠道操作结果：统一承载渠道主动调用(prepay/query/close/refund/queryRefund)与路由(resolve)的成功/失败。
 *
 * @param success 是否成功(通信层/路由层成功,不代表业务成功;退款业务状态见 {@code RefundResult.status})
 * @param data    成功时的载荷,失败时为 null
 * @param errMsg  失败描述,成功时为 null
 * @author wzkris
 */
public record ChannelResult<T>(boolean success, T data, String errMsg) {

    public static <T> ChannelResult<T> ok(T data) {
        return new ChannelResult<>(true, data, null);
    }

    public static <T> ChannelResult<T> fail(String errMsg) {
        return new ChannelResult<>(false, null, errMsg);
    }

}
