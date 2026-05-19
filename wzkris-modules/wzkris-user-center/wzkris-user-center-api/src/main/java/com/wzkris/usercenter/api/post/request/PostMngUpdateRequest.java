package com.wzkris.usercenter.api.post.request;

import com.wzkris.usercenter.enums.post.PostStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import java.util.List;

@Data
@Schema(description = "修改职位参数体")
public class PostMngUpdateRequest {

    @NotNull(message = "{invalidParameter.id.invalid}")
    private Long postId;

    @Size(min = 2, max = 20, message = "{invalidParameter.roleName.invalid}")
    @Schema(description = "职位名称")
    private String postName;

    @Schema(description = "状态")
    private PostStatusEnum status;

    @Range(message = "{invalidParameter.sort.invalid}")
    @Schema(description = "职位排序")
    private Integer postSort;

    @Schema(description = "菜单组")
    private List<Long> menuIds;

}

