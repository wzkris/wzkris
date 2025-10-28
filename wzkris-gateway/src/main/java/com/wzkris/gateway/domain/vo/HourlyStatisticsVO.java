package com.wzkris.gateway.domain.vo;

import com.wzkris.gateway.domain.PvUv;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;

/**
 * 小时统计响应实体
 *
 * @author wzkris
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HourlyStatisticsVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 认证类型
     */
    private String authType;

    /**
     * 统计日期
     */
    private String date;

    /**
     * 24小时统计数据（小时 -> PV/UV统计）
     */
    private Map<String, PvUv> hourlyData;

}

