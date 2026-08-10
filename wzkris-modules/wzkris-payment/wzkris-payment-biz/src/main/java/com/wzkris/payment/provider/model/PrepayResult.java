package com.wzkris.payment.provider.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 渠道预下单结果（provider 返回，由编排层组装为对外 {@code PayOrderCreateResponse}）
 *
 * <p>provider 只负责产出渠道侧支付参数，订单字段由编排层填充，避免响应跨两层拼装。
 *
 * @author wzkris
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrepayResult {

    /**
     * 渠道侧支付参数（前端据此唤起支付：NATIVE=code_url、H5=h5_url、JSAPI/APP=二次签名唤起参数JSON）
     */
    private String prepayPayload;

}
