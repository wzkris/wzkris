package com.wzkris.payment.provider.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

/**
 * 渠道退款结果
 *
 * @author wzkris
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefundResult {

    private RefundResultStatus status;

    private String channelRefundNo;

    private OffsetDateTime refundAt;

    private String rawResponse;

    private String errorMsg;
}
