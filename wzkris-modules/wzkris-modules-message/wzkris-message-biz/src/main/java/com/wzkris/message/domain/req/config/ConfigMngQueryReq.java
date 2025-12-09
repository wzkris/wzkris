package com.wzkris.message.domain.req.config;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "筛选条件")
public class ConfigMngQueryReq {

    @Schema(description = "参数名称")
    private String configName;

    @Schema(description = "参数键名")
    private String configKey;

    @Schema(description = "配置类型")
    private String configType;

    @Schema(description = "是否内置")
    private Boolean builtIn;

}
