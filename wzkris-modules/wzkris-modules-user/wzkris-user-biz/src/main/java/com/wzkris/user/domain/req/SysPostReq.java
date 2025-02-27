package com.wzkris.user.domain.req;

import com.wzkris.common.core.annotation.EnumsCheck;
import com.wzkris.common.core.constant.CommonConstants;
import com.wzkris.user.domain.SysPost;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMappers;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

@Data
@AutoMappers({@AutoMapper(target = SysPost.class)})
@Schema(description = "系统岗位添加修改参数体")
public class SysPostReq {

    private Long postId;

    @Length(min = 1, max = 30, message = "{validate.size.illegal}")
    @Schema(description = "岗位编码")
    private String postCode;

    @NotBlank(message = "{desc.post}{desc.name}" + "{validate.notnull}")
    @Size(min = 2, max = 30, message = "{desc.post}{desc.name}" + "{validate.size.illegal}")
    @Schema(description = "岗位名称")
    private String postName;

    @EnumsCheck(values = {CommonConstants.STATUS_ENABLE, CommonConstants.STATUS_DISABLE})
    @Schema(description = "状态（0正常 1停用）")
    private String status;

    @NotNull(message = "{desc.post}{desc.sort}" + "{validate.notnull}")
    @Range(max = Integer.MAX_VALUE, message = "{desc.sort}" + "{validate.size.illegal}")
    @Schema(description = "岗位排序")
    private Integer postSort;
}
