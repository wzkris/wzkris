package com.wzkris.equipment.mqtt.model;

import com.fasterxml.jackson.databind.node.ObjectNode;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import lombok.Data;

@Data
@Schema(description = "请求模型")
public class ThingsRequest {

    @Nullable
    @Schema(description = "请求序列号")
    private String id;

    @Nullable
    @Schema(description = "协议版本")
    private String version;

    @Schema(description = "请求参数")
    private ObjectNode params;

    @Schema(description = "请求方法")
    private String method;
}
