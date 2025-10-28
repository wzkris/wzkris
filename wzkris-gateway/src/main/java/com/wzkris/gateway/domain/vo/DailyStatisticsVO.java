package com.wzkris.gateway.domain.vo;

import com.wzkris.gateway.domain.PvUv;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 日统计响应实体
 *
 * @author wzkris
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyStatisticsVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 统计日期
     */
    private String date;

    /**
     * 认证类型
     */
    private String authType;

    /**
     * 统计数据
     */
    private PvUv statistics;

}

