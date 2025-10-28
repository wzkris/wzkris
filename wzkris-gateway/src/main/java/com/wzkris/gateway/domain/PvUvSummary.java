package com.wzkris.gateway.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * pv/uv统计汇总
 *
 * @author wzkris
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PvUvSummary implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户统计
     */
    private PvUv user;

    /**
     * 员工统计
     */
    private PvUv staff;

    /**
     * 客户统计
     */
    private PvUv customer;

    /**
     * 匿名用户统计
     */
    private PvUv anonymous;

    /**
     * 总计统计
     */
    private PvUv total;

    /**
     * 统计日期
     */
    private String date;

    /**
     * 计算总计统计
     */
    public PvUv getTotal() {
        if (total != null) {
            return total;
        }

        long totalPv = getValue(user, PvUv::getPv) +
                getValue(staff, PvUv::getPv) +
                getValue(customer, PvUv::getPv) +
                getValue(anonymous, PvUv::getPv);

        long totalUv = getValue(user, PvUv::getUv) +
                getValue(staff, PvUv::getUv) +
                getValue(customer, PvUv::getUv) +
                getValue(anonymous, PvUv::getUv);

        long totalSuccess = getValue(user, PvUv::getSuccessCount) +
                getValue(staff, PvUv::getSuccessCount) +
                getValue(customer, PvUv::getSuccessCount) +
                getValue(anonymous, PvUv::getSuccessCount);

        long totalError = getValue(user, PvUv::getErrorCount) +
                getValue(staff, PvUv::getErrorCount) +
                getValue(customer, PvUv::getErrorCount) +
                getValue(anonymous, PvUv::getErrorCount);

        return PvUv.builder()
                .pv(totalPv)
                .uv(totalUv)
                .successCount(totalSuccess)
                .errorCount(totalError)
                .build();
    }

    private long getValue(PvUv data, java.util.function.Function<PvUv, Long> getter) {
        if (data == null) {
            return 0L;
        }
        Long value = getter.apply(data);
        return value != null ? value : 0L;
    }

}
