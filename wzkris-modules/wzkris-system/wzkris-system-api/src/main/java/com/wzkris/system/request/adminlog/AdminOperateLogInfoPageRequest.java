package com.wzkris.system.request.adminlog;

import com.wzkris.common.core.model.QueryRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "筛选条件")
public class AdminOperateLogInfoPageRequest extends QueryRequest {

    @Schema(description = "操作模块")
    private String title;

    @Schema(description = "子模块")
    private String subTitle;

    @Schema(description = "操作类型")
    private String operType;

    @Schema(description = "操作人员")
    private String operName;

    @Schema(description = "操作状态")
    private Boolean success;

}

