package com.wzkris.gateway.domain.vo;

import com.wzkris.gateway.domain.PvUv;
import com.wzkris.gateway.domain.PvUvSummary;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;

/**
 * 实时统计响应实体
 *
 * @author wzkris
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RealtimeStatisticsVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 时间戳
     */
    private Long timestamp;

    /**
     * 统计日期
     */
    private String date;

    /**
     * 当前小时统计（按认证类型分组，包含PV和UV）
     */
    private Map<String, PvUv> currentHour;

    /**
     * 今日汇总统计
     */
    private PvUvSummary today;

}

