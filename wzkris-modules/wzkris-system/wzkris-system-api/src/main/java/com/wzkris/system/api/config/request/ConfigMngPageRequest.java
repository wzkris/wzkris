package com.wzkris.system.api.config.request;

import com.wzkris.common.orm.request.PagingRequest;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "筛选条件")
public class ConfigMngPageRequest extends PagingRequest {

    @Parameter(description = "参数名称")
    private String configName;

    @Parameter(description = "参数键名")
    private String configKey;

    @Parameter(description = "配置类型")
    private String configType;

    @Parameter(description = "是否内置")
    private Boolean builtIn;

}
