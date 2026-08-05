package com.wzkris.usercenter.api.config.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ConfigInfoResponse {

    private Long id;

    @Schema(description = "参数名称")
    private String configName;

    @Schema(description = "参数键名")
    private String configKey;

    @Schema(description = "参数键值")
    private String configValue;

    @Schema(description = "配置类型")
    private String configType;

    @Schema(description = "是否内置")
    private Boolean builtIn;

}
