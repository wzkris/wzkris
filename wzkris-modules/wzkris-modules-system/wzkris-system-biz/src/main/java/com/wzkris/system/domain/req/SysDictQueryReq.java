package com.wzkris.system.domain.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "筛选条件")
public class SysDictQueryReq {

    @Schema(description = "字典键")
    private String dictKey;

    @Schema(description = "字典名称")
    private String dictName;

}
