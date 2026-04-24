package com.wzkris.usercenter.request.dept;

import com.wzkris.usercenter.enums.dept.DeptStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

/**
 * 修改部门请求体
 */
@Data
@Schema(description = "修改部门参数体")
public class DeptMngUpdateRequest {

    @NotNull(message = "{invalidParameter.id.invalid}")
    private Long deptId;

    @Schema(description = "租户 ID")
    private Long tenantId;

    @Schema(description = "父部门 ID")
    private Long parentId;

    @Schema(description = "祖级列表")
    private Long[] ancestors;

    @Size(min = 2, max = 30, message = "{invalidParameter.deptName.invalid}")
    @Schema(description = "部门名称")
    private String deptName;

    @Schema(description = "0 代表存在 1 代表停用")
    private DeptStatusEnum status;

    @Range(max = Integer.MAX_VALUE, message = "{invalidParameter.sort.invalid}")
    @Schema(description = "显示顺序")
    private Integer deptSort;

    @Size(min = 6, max = 15, message = "{invalidParameter.phonenumber.invalid}")
    @Schema(description = "联系电话")
    private String contact;

    @Email(message = "{invalidParameter.email.invalid}")
    @Schema(description = "部门邮箱")
    private String email;

}

