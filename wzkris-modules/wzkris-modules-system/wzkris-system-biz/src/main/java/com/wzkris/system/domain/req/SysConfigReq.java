package com.wzkris.system.domain.req;

import com.wzkris.common.core.annotation.EnumsCheck;
import com.wzkris.common.core.constant.CommonConstants;
import com.wzkris.system.domain.SysConfig;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMappers;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@AutoMappers({@AutoMapper(target = SysConfig.class)})
@Schema(description = "系统参数添加修改参数体")
public class SysConfigReq {

    private Long configId;

    @NotBlank(message = "{desc.system}{desc.config}{desc.name}" + "{validate.notnull}")
    @Size(min = 1, max = 50, message = "{desc.system}{desc.config}{desc.name}" + "{validate.size.illegal}")
    @Schema(description = "参数名称")
    private String configName;

    @NotBlank(message = "{desc.system}{desc.config}{desc.key}" + "{validate.notnull}")
    @Size(min = 3, max = 50, message = "{desc.system}{desc.config}{desc.key}" + "{validate.size.illegal}")
    @Schema(description = "参数键名")
    private String configKey;

    @NotBlank(message = "{desc.system}{desc.config}{desc.value}" + "{validate.notnull}")
    @Size(min = 1, max = 500, message = "{desc.system}{desc.config}{desc.value}" + "{validate.size.illegal}")
    @Schema(description = "参数键值")
    private String configValue;

    @EnumsCheck(values = {CommonConstants.YES, CommonConstants.NO})
    @Schema(description = "系统内置（Y是 N否）")
    private String configType;
}
