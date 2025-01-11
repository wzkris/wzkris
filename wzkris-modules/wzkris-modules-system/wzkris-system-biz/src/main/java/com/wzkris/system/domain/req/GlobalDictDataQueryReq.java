package com.wzkris.system.domain.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "筛选条件")
public class GlobalDictDataQueryReq {

    @Schema(description = "标签")
    private String dictLabel;

    @Schema(description = "字典类型")
    private String dictType;

    @Schema(description = "是否默认（Y是 N否")
    private String isDefault;
}
