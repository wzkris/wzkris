package com.wzkris.usercenter.request.post;

import com.wzkris.common.core.constant.CommonConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import java.util.List;

/**
 * 修改职位请求体
 */
@Data
@Schema(description = "修改职位参数体")
public class PostMngUpdateRequest {

    @NotNull(message = "{invalidParameter.id.invalid}")
    private Long postId;

    @NotBlank(message = "{invalidParameter.roleName.invalid}")
    @Size(min = 2, max = 20, message = "{invalidParameter.roleName.invalid}")
    @Schema(description = "职位名称")
    private String postName;

    @NotBlank(message = "{invalidParameter.status.invalid}")
    @Pattern(
            regexp = "[" +
                    CommonConstants.STATUS_ENABLE +
                    CommonConstants.STATUS_DISABLE
                    + "]",
            message = "{invalidParameter.status.invalid}")
    @Schema(description = "状态（0 代表正常 1 代表停用）")
    private String status;

    @NotNull(message = "{invalidParameter.sort.invalid}")
    @Range(message = "{invalidParameter.sort.invalid}")
    @Schema(description = "职位排序")
    private Integer postSort;

    @Schema(description = "菜单组")
    private List<Long> menuIds;

}

