package com.wzkris.usercenter.api.member.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wzkris.usercenter.enums.member.MemberStatusEnum;
import com.wzkris.usercenter.enums.user.GenderEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

/**
 * @author : wzkris
 * @version : V1.0.1
 * @description : 租户成员展示层对象
 * @date : 2025/10/9 14:00
 */
@Data
@NoArgsConstructor
public class MemberMngResponse {

    private Long id;

    @Schema(description = "租户ID")
    private Long tenantId;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "手机号码")
    private String phoneNumber;

    @Schema(description = "状态值")
    private MemberStatusEnum status;

    @Schema(description = "性别")
    private GenderEnum gender;

    @Schema(description = "头像")
    private String avatar;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Schema(description = "密码")
    private String password;

    @Schema(description = "最近登录ip")
    private String loginIp;

    @Schema(description = "最近登录日期")
    private OffsetDateTime loginDate;

    @Schema(description = "额外信息")
    private String remark;

    @Schema(description = "职位名称")
    private String postName;

}

