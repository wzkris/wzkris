package com.wzkris.system.domain.req.config;

import com.wzkris.common.core.constant.CommonConstants;
import com.wzkris.common.validator.annotation.EnumsCheck;
import com.wzkris.system.domain.ConfigInfoDO;
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

    @EnumsCheck(values = {CommonConstants.YES, CommonConstants.NO},
            message = "{invalidParameter.configType.invalid}")
    @Schema(description = "系统内置（Y是 N否）")
    private String configType;

}
