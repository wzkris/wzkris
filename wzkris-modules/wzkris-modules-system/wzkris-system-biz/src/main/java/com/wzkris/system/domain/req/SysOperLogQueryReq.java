package com.wzkris.system.domain.req;

import com.wzkris.common.web.model.QueryReq;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "筛选条件")
public class SysOperLogQueryReq extends QueryReq {

    @Schema(description = "操作模块")
    private String title;

    @Schema(description = "子模块")
    private String subTitle;

    @Schema(description = "操作类型（0其它 1新增 2修改 3删除）")
    private String operType;

    @Schema(description = "操作人员")
    private String operName;

    @Schema(description = "操作状态（0正常 1异常）")
    private String status;
}
