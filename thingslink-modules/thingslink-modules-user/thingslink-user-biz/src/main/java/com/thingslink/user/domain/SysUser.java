package com.thingslink.user.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.thingslink.common.core.annotation.PhoneNumber;
import com.thingslink.common.core.annotation.Xss;
import com.thingslink.common.core.annotation.group.ValidationGroups;
import com.thingslink.common.core.constant.SecurityConstants;
import com.thingslink.common.orm.model.BaseEntity;
import com.thingslink.common.security.oauth2.model.LoginSysUser;
import com.thingslink.user.api.domain.dto.SysUserDTO;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMappers;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户对象 user
 *
 * @author wzkris
 */
@Data
@Accessors(chain = true)
@AutoMappers({
        @AutoMapper(target = LoginSysUser.class),
        @AutoMapper(target = SysUserDTO.class)
})
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@FieldNameConstants
public class SysUser extends BaseEntity {

    @TableId
    private Long userId;

    @Schema(description = "租户ID")
    private Long tenantId;

    @Schema(description = "部门ID")
    private Long deptId;

    @Xss
    @NotBlank(message = "[username] {validate.notnull}")
    @Size(min = 0, max = 30, message = "[username] {validate.size.illegal}")
    @Schema(description = "用户名")
    private String username;

    @Xss
    @Schema(description = "用户昵称")
    private String nickname;

    @Email(message = "[email] {validate.email.illegal}")
    @Size(min = 0, max = 50, message = "[email] {validate.size.illegal}")
    @Schema(description = "用户邮箱")
    private String email;

    @PhoneNumber
    @Schema(description = "手机号码")
    private String phoneNumber;

    @Schema(description = "用户状态")
    private String status;

    @Schema(description = "用户性别")
    private String gender;

    @Schema(description = "用户头像")
    private String avatar;

    @NotBlank(message = "[password] {validate.notnull}", groups = ValidationGroups.Insert.class)
    @Size(min = 6, max = 32, message = "[password] {validate.size.illegal}")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Schema(description = "密码")
    private String password;

    @Schema(description = "删除标志")
    private Boolean isDeleted;

    @Schema(description = "最近登录ip")
    private String loginIp;

    @Schema(description = "最近登录日期")
    private Long loginDate;

    @Schema(description = "用户额外信息")
    private String remark;

    @JsonIgnore
    @TableField(exist = false)
    @Schema(description = "请求参数")
    private Map<String, Object> params;

    public Map<String, Object> getParams() {
        if (params == null) {
            params = new HashMap<>(2);
        }
        return params;
    }

    public SysUser(Long userId) {
        this.userId = userId;
    }

    public static boolean isSuperAdmin(Long userId) {
        return SecurityConstants.SUPER_ADMIN_ID.equals(userId);
    }

    public boolean isSuperAdmin() {
        return isSuperAdmin(this.userId);
    }

}
