package com.wzkris.payment.provider.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

/**
 * 渠道查单结果
 *
 * @author wzkris
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PayQueryResult {

    /**
     * 渠道侧是否已支付
     */
    private boolean paid;

    private String channelOrderNo;

    private BigDecimal amount;

    private OffsetDateTime payAt;

    private String rawResponse;

}
