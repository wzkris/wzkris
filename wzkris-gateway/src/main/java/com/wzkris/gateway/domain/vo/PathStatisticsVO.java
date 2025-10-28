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
 * 路径统计响应实体
 *
 * @author wzkris
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PathStatisticsVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 请求路径
     */
    private String path;

    /**
     * 统计日期
     */
    private String date;

    /**
     * 按认证类型分组的统计数据
     */
    private Map<String, PvUv> stats;

}

