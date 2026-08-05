package com.wzkris.usercenter.api.post.response;

import com.wzkris.usercenter.enums.post.PostStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PostMngResponse {

    private Long id;

    @Schema(description = "租户ID")
    private Long tenantId;

    @Schema(description = "职位名称")
    private String postName;

    @Schema(description = "状态（0代表正常 1代表停用）")
    private PostStatusEnum status;

    @Schema(description = "角色排序")
    private Integer postSort;

}
