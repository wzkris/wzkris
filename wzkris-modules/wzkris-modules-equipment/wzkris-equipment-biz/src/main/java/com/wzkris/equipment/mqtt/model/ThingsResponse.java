package com.wzkris.equipment.mqtt.model;

import com.fasterxml.jackson.databind.node.ObjectNode;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import lombok.Data;

@Data
@Schema(description = "响应模型")
public class ThingsResponse {

    @Schema(description = "响应状态")
    private String code;

    @Schema(description = "响应数据")
    private ObjectNode data;

    @Nullable
    @Schema(description = "请求序列号")
    private String id;

    @Schema(description = "请求方法")
    private String method;
}
