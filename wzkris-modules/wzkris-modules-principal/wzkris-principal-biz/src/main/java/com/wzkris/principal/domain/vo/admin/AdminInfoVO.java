package com.wzkris.principal.domain.vo.admin;

import com.wzkris.common.core.annotation.Sensitive;
import com.wzkris.common.core.enums.SensitiveStrategyEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Collection;
import java.util.Date;

@Data
@Schema(description = "管理员信息")
public class AdminInfoVO {

    @Schema(description = "是否超级管理员")
    private boolean admin;

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

    @Sensitive(strategy = SensitiveStrategyEnum.EMAIL)
    @Schema(description = "用户邮箱")
    private String email;

    @Sensitive(strategy = SensitiveStrategyEnum.PHONE)
    @Schema(description = "手机号码")
    private String phoneNumber;

    @Schema(description = "用户性别")
    private String gender;

    @Schema(description = "登录时间")
    private Date loginDate;

}
