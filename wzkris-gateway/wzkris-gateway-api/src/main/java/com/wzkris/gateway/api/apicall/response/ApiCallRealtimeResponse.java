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
public class ApiCallRealtimeResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "滑动窗口秒数")
    private Integer windowSeconds;

    @Schema(description = "窗口内总请求数")
    private Long requestCount;

    @Schema(description = "当前QPS（窗口总请求/窗口秒数）")
    private Double qps;

    @Schema(description = "成功QPS")
    private Double successQps;

    @Schema(description = "失败QPS")
    private Double errorQps;

    @Schema(description = "成功率（0-1范围）")
    private Double successRate;

    @Schema(description = "平均耗时（毫秒）")
    private Long avgCostMs;

    @Schema(description = "最大耗时（毫秒）")
    private Long maxCostMs;

    @Schema(description = "HTTP 2xx状态码计数")
    private Long status2xxCount;

    @Schema(description = "HTTP 3xx状态码计数")
    private Long status3xxCount;

    @Schema(description = "HTTP 4xx状态码计数")
    private Long status4xxCount;

    @Schema(description = "HTTP 5xx状态码计数")
    private Long status5xxCount;

    @Schema(description = "HTTP方法计数，如GET/POST/PUT/DELETE")
    private Map<String, Long> methodCounts;

}
