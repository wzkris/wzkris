package com.wzkris.principal.domain.req.admin;

import com.wzkris.common.core.constant.CommonConstants;
import com.wzkris.common.validator.annotation.EnumsCheck;
import com.wzkris.common.validator.annotation.PhoneNumber;
import com.wzkris.common.validator.annotation.Xss;
import com.wzkris.common.validator.group.ValidationGroups;
import com.wzkris.principal.domain.AdminInfoDO;
import com.wzkris.principal.enums.GenderEnum;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMappers;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
@AutoMappers({@AutoMapper(target = AdminInfoDO.class)})
@Schema(description = "管理添加修改参数体")
public class AdminMngReq {

    private Long adminId;

    @Schema(description = "部门ID")
    private Long deptId;

    @Pattern(regexp = "^[a-z0-9_]+$", message = "{invalidParameter.username.invalid}")// 用户名只能为小写英文、数字和下划线
    @Xss
    @NotBlank(message = "{invalidParameter.username.invalid}", groups = ValidationGroups.Insert.class)
    @Size(min = 6, max = 30, message = "{invalidParameter.username.invalid}")
    @Schema(description = "用户名")
    private String username;

    @Xss
    @Schema(description = "用户昵称")
    private String nickname;

    @Email(message = "{invalidParameter.email.invalid}")
    @Schema(description = "用户邮箱")
    private String email;

    @PhoneNumber
    @Schema(description = "手机号码")
    private String phoneNumber;

    @Pattern(
            regexp = "[" +
                    CommonConstants.STATUS_ENABLE +
                    CommonConstants.STATUS_DISABLE
                    + "]",
            message = "{invalidParameter.status.invalid}")
    @Schema(description = "用户状态")
    private String status;

    @EnumsCheck(value = GenderEnum.class, property = "value")
    @Schema(description = "用户性别")
    private String gender;

    @Schema(description = "用户额外信息")
    private String remark;

    @Schema(description = "角色组")
    private List<Long> roleIds;

}
