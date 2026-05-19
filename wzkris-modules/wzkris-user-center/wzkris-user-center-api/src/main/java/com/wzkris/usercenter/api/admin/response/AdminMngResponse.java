package com.wzkris.usercenter.api.admin.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wzkris.usercenter.enums.admin.AdminStatusEnum;
import com.wzkris.usercenter.enums.dept.DeptStatusEnum;
import com.wzkris.usercenter.enums.user.GenderEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 管理员展示层对象
 * @date : 2023/5/26 16:12
 */
@Data
@NoArgsConstructor
public class AdminMngResponse {

    private Long adminId;

    @Schema(description = "部门ID")
    private Long deptId;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "用户昵称")
    private String nickname;

    @Schema(description = "用户邮箱")
    private String email;

    @Schema(description = "手机号码")
    private String phoneNumber;

    @Schema(description = "用户状态")
    private AdminStatusEnum status;

    @Schema(description = "用户性别")
    private GenderEnum gender;

    @Schema(description = "用户头像")
    private String avatar;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Schema(description = "密码")
    private String password;

    @Schema(description = "最近登录ip")
    private String loginIp;

    @Schema(description = "最近登录日期")
    private OffsetDateTime loginDate;

    @Schema(description = "用户额外信息")
    private String remark;

    @Schema(description = "部门名称")
    private String deptName;

    @Schema(description = "部门状态")
    private DeptStatusEnum deptStatus;

}

