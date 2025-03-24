package com.wzkris.system.domain.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "筛选条件")
public class SysMessageQueryReq {

    @Schema(description = "标题")
    private String title;

    @Schema(description = "状态（0正常 1关闭）")
    private String status;
}
