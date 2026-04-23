package com.wzkris.gateway.domain.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;

/**
 * 页面访问（日级）统计响应，包含24小时序列
 *
 * @author wzkris
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageViewDailySeriesResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "日期（格式：yyyy-MM-dd）")
    private String date;

    @Schema(description = "当日总计")
    private PageViewResponse total;

    @Schema(description = "小时序列，key为yyyy-MM-dd-HH，value为该小时的PV/UV统计")
    private Map<String, PageViewResponse> hours;

}

