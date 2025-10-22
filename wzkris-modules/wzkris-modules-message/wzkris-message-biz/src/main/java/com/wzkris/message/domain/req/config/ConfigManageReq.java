package com.wzkris.message.domain.req.config;

import com.wzkris.message.domain.ConfigInfoDO;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMappers;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@AutoMappers({@AutoMapper(target = ConfigInfoDO.class)})
@Schema(description = "系统参数添加修改参数体")
public class ConfigManageReq {

    private Long configId;

    @NotBlank(message = "{invalidParameter.configName.invalid}")
    @Size(min = 1, max = 50, message = "{invalidParameter.configName.invalid}")
    @Schema(description = "参数名称")
    private String configName;

    @NotBlank(message = "{invalidParameter.configKey.invalid}")
    @Size(min = 3, max = 50, message = "{invalidParameter.configKey.invalid}")
    @Schema(description = "参数键名")
    private String configKey;

    @NotBlank(message = "{invalidParameter.configValue.invalid}")
    @Size(min = 1, max = 500, message = "{invalidParameter.configValue.invalid}")
    @Schema(description = "参数键值")
    private String configValue;

    @Schema(description = "配置类型")
    private String configType;

    @Schema(description = "是否内置")
    private Boolean builtIn;

}
