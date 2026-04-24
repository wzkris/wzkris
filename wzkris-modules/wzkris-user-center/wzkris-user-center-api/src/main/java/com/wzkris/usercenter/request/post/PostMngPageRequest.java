package com.wzkris.usercenter.request.post;

import com.wzkris.usercenter.enums.post.PostStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "职位管理查询参数体")
public class PostMngPageRequest {

    @Schema(description = "职位名称")
    private String postName;

    @Schema(description = "状态（0代表正常 1代表停用）")
    private PostStatusEnum status;

}

