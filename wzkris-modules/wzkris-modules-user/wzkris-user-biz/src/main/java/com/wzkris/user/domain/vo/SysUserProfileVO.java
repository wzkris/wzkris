package com.wzkris.user.domain.vo;

import com.wzkris.common.core.annotation.Sensitive;
import com.wzkris.common.core.annotation.impl.SensitiveStrategy;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;
import lombok.Data;

@Data
@Schema(description = "系统用户信息")
public class SysUserProfileVO {

    @Schema(description = "用户信息")
    private UserInfo user;

    @Schema(description = "所在部门")
    private String deptName;

    @Schema(description = "角色信息")
    private String roleGroup;

    @Schema(description = "岗位信息")
    private String postGroup;

    @Data
    public static class UserInfo {

        @Schema(description = "用户名")
        private String username;

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
}
