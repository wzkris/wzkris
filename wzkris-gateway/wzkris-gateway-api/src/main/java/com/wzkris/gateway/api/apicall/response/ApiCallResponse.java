package com.wzkris.gateway.api.apicall.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiCallResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "API接口调用总次数")
    private Long apiCallCount;

    @Schema(description = "调用成功次数")
    private Long successCount;

    @Schema(description = "调用失败次数")
    private Long errorCount;

    @Schema(description = "HTTP 2xx状态码计数")
    private Long status2xxCount;

    @Schema(description = "HTTP 3xx状态码计数")
    private Long status3xxCount;

    @Schema(description = "HTTP 4xx状态码计数")
    private Long status4xxCount;

    @Schema(description = "HTTP 5xx状态码计数")
    private Long status5xxCount;

    @Schema(description = "总耗时（毫秒）")
    private Long totalCostMs;

    @Schema(description = "平均耗时（毫秒）")
    private Long avgCostMs;

    @Schema(description = "最大耗时（毫秒）")
    private Long maxCostMs;

    @Schema(description = "HTTP方法计数，如GET/POST/PUT/DELETE")
    private Map<String, Long> methodCounts;

}
