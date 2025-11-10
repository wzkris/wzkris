package com.wzkris.principal.domain.vo.member;

import com.wzkris.common.core.annotation.Sensitive;
import com.wzkris.common.core.enums.SensitiveStrategy;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Collection;
import java.util.Date;

@Data
@Schema(description = "租户成员信息")
public class MemberInfoVO {

    @Schema(description = "是否管理员")
    private boolean admin;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "权限")
    private Collection<String> authorities;

    @Schema(description = "职位")
    private String postGroup;

    @Schema(description = "头像")
    private String avatar;

    @Sensitive(strategy = SensitiveStrategy.PHONE)
    @Schema(description = "手机号码")
    private String phoneNumber;

    @Schema(description = "用户性别")
    private String gender;

    @Schema(description = "登录时间")
    private Date loginDate;

}
