package com.wzkris.usercenter.api.tenantuser.request;

import com.wzkris.common.validator.annotation.PhoneNumber;
import com.wzkris.common.validator.annotation.Xss;
import com.wzkris.usercenter.enums.tenantuser.TenantUserStatusEnum;
import com.wzkris.usercenter.enums.user.GenderEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "新增租户用户参数体")
public class TenantUserMngSaveRequest {

    @Pattern(regexp = "^[a-z0-9_]+$", message = "{invalidParameter.username.invalid}")// 用户名只能为小写英文、数字和下划线
    @Xss
    @NotBlank(message = "{invalidParameter.username.invalid}")
    @Size(min = 6, max = 30, message = "{invalidParameter.username.invalid}")
    @Schema(description = "用户名")
    private String username;

    @PhoneNumber
    @Schema(description = "手机号码")
    private String phoneNumber;

    @Schema(description = "状态")
    private TenantUserStatusEnum status;

    @Schema(description = "性别")
    private GenderEnum gender;

    @Schema(description = "用户额外信息")
    private String remark;

    @Schema(description = "角色组")
    private List<Long> tenantRoleIds;

}

