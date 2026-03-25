package com.wzkris.usercenter.request.post;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "职位管理查询参数体")
public class PostMngQueryRequest {

    @Schema(description = "职位名称")
    private String postName;

    @Schema(description = "状态（0代表正常 1代表停用）")
    private String status;

}

