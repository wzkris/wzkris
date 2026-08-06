package com.wzkris.usercenter.api.dictionary.request;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "筛选条件")
public class DictionaryPublicListRequest {

    @Parameter(in = ParameterIn.PATH, required = true, description = "字典键")
    private String dictKey;

    @Parameter(description = "字典名称")
    private String dictName;

}
