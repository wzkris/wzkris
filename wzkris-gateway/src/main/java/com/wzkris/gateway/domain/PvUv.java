package com.wzkris.gateway.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * pv/uv统计数据
 *
 * @author wzkris
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PvUv implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 页面访问量
     */
    private Long pv;

    /**
     * 独立访客数
     */
    private Long uv;

    /**
     * 成功请求数
     */
    private Long successCount;

    /**
     * 错误请求数
     */
    private Long errorCount;

    /**
     * 计算成功率（百分比）
     */
    public Double getSuccessRate() {
        if (pv == null || pv == 0) {
            return 0.0;
        }
        if (successCount == null) {
            return 0.0;
        }
        return (double) successCount / pv * 100;
    }

}
