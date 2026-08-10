package com.wzkris.payment.util;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.wzkris.payment.properties.PaymentProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 业务可读订单号生成器：前缀 + yyyyMMddHHmmss + 雪花后 8 位
 *
 * <p>不直接用纯雪花：加日期前缀后人工可辨，便于对账。唯一性由 DB 部分唯一索引兜底。
 *
 * @author wzkris
 */
@Component
@RequiredArgsConstructor
public class OrderNoGenerator {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    private final PaymentProperties properties;

    /**
     * 生成支付订单号
     */
    public String nextOrderNo() {
        return nextNo(properties.getOrderNoPrefix());
    }

    /**
     * 生成退款单号
     */
    public String nextRefundNo() {
        return nextNo(properties.getRefundNoPrefix());
    }

    /**
     * 前缀 + 秒级时间 + 雪花后 8 位
     */
    private String nextNo(String prefix) {
        return prefix
                + FORMATTER.format(LocalDateTime.now())
                + String.format("%08d", Math.abs(IdWorker.getId() % 100_000_000L));
    }

}
