package com.wzkris.track.api.track.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Map;

@Data
@Schema(description = "单条埋点事件")
public class TrackEventRequest {

    @NotBlank
    @Schema(description = "事件名")
    private String event;

    @Schema(description = "客户端时间戳(ms)")
    private Long ts;

    @NotBlank
    @Schema(description = "匿名设备/用户标识")
    private String distinctId;

    @Schema(description = "会话标识")
    private String sessionId;

    @Schema(description = "端类型")
    private String platform;

    @Schema(description = "自定义属性")
    private Map<String, Object> properties;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "服务端接收时间(ms)", accessMode = Schema.AccessMode.READ_ONLY)
    private Long serverReceivedAt;

}
