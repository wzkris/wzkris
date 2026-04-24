package com.wzkris.gateway.domain.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "API调用实时统计查询参数")
public class ApiCallRealtimeQueryRequest {

    @Schema(description = "认证类型")
    private String authType;

    @Schema(description = "时间窗口（秒）")
    private Integer windowSeconds = 60;

}
