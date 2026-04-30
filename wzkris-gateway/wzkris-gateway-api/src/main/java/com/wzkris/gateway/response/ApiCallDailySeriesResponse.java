package com.wzkris.gateway.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;

/**
 * API调用（日级）统计响应，包含24小时序列
 *
 * @author wzkris
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiCallDailySeriesResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "日期（格式：yyyy-MM-dd）")
    private String date;

    @Schema(description = "当日总计")
    private ApiCallResponse total;

    @Schema(description = "小时序列，key为yyyy-MM-dd-HH，value为该小时的API调用统计")
    private Map<String, ApiCallResponse> hours;

    @Schema(description = "按路径的当日总计，key为API路径，value为该路径的统计信息")
    private Map<String, ApiCallResponse> paths;

}

