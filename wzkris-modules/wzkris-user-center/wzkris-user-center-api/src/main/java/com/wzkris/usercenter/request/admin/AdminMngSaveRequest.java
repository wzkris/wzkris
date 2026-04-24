package com.wzkris.usercenter.request.admin;

import com.wzkris.common.validator.annotation.EnumsCheck;
import com.wzkris.common.validator.annotation.PhoneNumber;
import com.wzkris.common.validator.annotation.Xss;
import com.wzkris.usercenter.enums.admin.AdminStatusEnum;
import com.wzkris.usercenter.enums.user.GenderEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

/**
 * 新增管理员请求体
 */
@Data
@Schema(description = "新增管理员参数体")
public class AdminMngSaveRequest {

    @Schema(description = "部门ID")
    private Long deptId;

    @Pattern(regexp = "^[a-z0-9_]+$", message = "{invalidParameter.username.invalid}")// 用户名只能为小写英文、数字和下划线
    @Xss
    @NotBlank(message = "{invalidParameter.username.invalid}")
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

    @Schema(description = "用户状态")
    private AdminStatusEnum status;

    @EnumsCheck(value = GenderEnum.class, property = "value")
    @Schema(description = "用户性别")
    private GenderEnum gender;

    @Schema(description = "用户额外信息")
    private String remark;

    @Schema(description = "角色组")
    private List<Long> roleIds;

}

