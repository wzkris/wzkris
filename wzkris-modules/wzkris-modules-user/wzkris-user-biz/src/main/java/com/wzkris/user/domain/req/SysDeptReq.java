package com.wzkris.user.domain.req;

import com.wzkris.common.core.annotation.EnumsCheck;
import com.wzkris.common.core.annotation.group.ValidationGroups;
import com.wzkris.common.core.constant.CommonConstants;
import com.wzkris.user.domain.SysDept;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMappers;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

@Data
@AutoMappers({@AutoMapper(target = SysDept.class)})
@Schema(description = "系统部门添加修改参数体")
public class SysDeptReq {

    @NotNull(groups = ValidationGroups.Update.class, message = "id {validate.notnull}")
    private Long deptId;

    @Schema(description = "租户ID")
    private Long tenantId;

    @Schema(description = "父部门ID")
    private Long parentId;

    @Schema(description = "祖级列表")
    private Long[] ancestors;

    @NotBlank(message = "{desc.dept}{desc.name}" + "{validate.notnull}")
    @Size(min = 2, max = 30, message = "{desc.dept}{desc.name}" + "{validate.size.illegal}")
    @Schema(description = "部门名称")
    private String deptName;

    @EnumsCheck(values = {CommonConstants.STATUS_DISABLE, CommonConstants.STATUS_ENABLE})
    @Schema(description = "0代表存在 1代表停用")
    private String status;

    @NotNull(message = "{desc.dept}{desc.sort}" + "{validate.notnull}")
    @Range(max = Integer.MAX_VALUE, message = "{desc.dept}{desc.sort}" + "{validate.size.illegal}")
    @Schema(description = "显示顺序")
    private Integer deptSort;

    @Size(min = 6, max = 15, message = "{desc.dept}{desc.phonenumber}" + "{validate.size.illegal}")
    @Schema(description = "联系电话")
    private String contact;

    @Email(message = "{desc.dept}" + "{validate.email.illegal}")
    @Schema(description = "部门邮箱")
    private String email;
}
