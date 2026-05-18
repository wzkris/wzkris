package com.wzkris.usercenter.request.post;

import com.wzkris.common.orm.request.PagingRequest;
import com.wzkris.usercenter.enums.post.PostStatusEnum;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "职位管理查询参数体")
public class PostMngPageRequest extends PagingRequest {

    @Parameter(description = "职位名称")
    private String postName;

    @Parameter(description = "状态（0代表正常 1代表停用）")
    private PostStatusEnum status;

}
