package com.wzkris.user.domain.vo;

import com.wzkris.common.validator.annotation.Sensitive;
import com.wzkris.common.validator.impl.SensitiveStrategy;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Collection;
import java.util.Date;

@Data
@Schema(description = "系统用户信息")
public class UserInfoVO {

    @Schema(description = "是否租户管理员")
    private boolean admin;

    @Schema(description = "是否超级租户")
    private boolean superTenant;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "权限")
    private Collection<String> authorities;

    @Schema(description = "所在部门")
    private String deptName;

    @Schema(description = "角色信息")
    private String roleGroup;

    @Schema(description = "头像")
    private String avatar;

    @Schema(description = "用户昵称")
    private String nickname;

    @Sensitive(strategy = SensitiveStrategy.EMAIL)
    @Schema(description = "用户邮箱")
    private String email;

    @Sensitive(strategy = SensitiveStrategy.PHONE)
    @Schema(description = "手机号码")
    private String phoneNumber;

    @Schema(description = "用户性别")
    private String gender;

    @Schema(description = "登录时间")
    private Date loginDate;

}
