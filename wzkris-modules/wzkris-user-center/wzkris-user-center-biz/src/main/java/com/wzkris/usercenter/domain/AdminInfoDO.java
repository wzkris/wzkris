package com.wzkris.usercenter.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wzkris.common.core.constant.SecurityConstants;
import com.wzkris.common.orm.model.BaseEntity;
import com.wzkris.usercenter.httpservice.admin.resp.adminInfoResp;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMappers;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 管理员DO
 *
 * @author wzkris
 */
@Data
@AutoMappers({@AutoMapper(target = adminInfoResp.class)})
@NoArgsConstructor
@TableName(schema = "biz", value = "admin_info")
public class AdminInfoDO extends BaseEntity {

    @TableId
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
    private String status;

    @Schema(description = "用户性别")
    private String gender;

    @Schema(description = "用户头像")
    private String avatar;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Schema(description = "密码")
    private String password;

    @Schema(description = "最近登录ip")
    private String loginIp;

    @Schema(description = "最近登录日期")
    private Date loginDate;

    @Schema(description = "用户额外信息")
    private String remark;

    public AdminInfoDO(Long adminId) {
        this.adminId = adminId;
    }

    public static boolean isSuperAdmin(Long adminId) {
        return SecurityConstants.SUPER_ADMIN_ID.equals(adminId);
    }

}
