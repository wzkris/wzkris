package com.wzkris.usercenter.api.config.request;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "查询请求")
public class ConfigInfoQueryRequest {

    @Parameter(in = ParameterIn.PATH, required = true, description = "参数键名")
    @NotBlank(message = "配置键名为必填项")
    private String configKey;

}
