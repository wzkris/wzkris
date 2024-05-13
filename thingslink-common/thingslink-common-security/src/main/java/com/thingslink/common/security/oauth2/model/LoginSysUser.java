package com.thingslink.common.security.oauth2.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.util.List;

/**
 * @author : wzkris
 * @version : V1.0.1
 * @description : 登录用户信息
 * @date : 2023/8/7 16:38
 * @UPDATE： 2024/4/9 09:29
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class LoginSysUser extends LoginUser {
    @Serial
    private static final long serialVersionUID = 3291189147544840626L;

    public static final String USER_TYPE = "sys_user";
    // 登录id
    private Long userId;
    // 部门id
    private Long deptId;
    // 租户id
    private Long tenantId;
    // 是否当前租户下的最高管理员
    private Boolean isAdmin;
    // 用户名
    private String username;
    // 密码
    private String password;
    // 部门数据权限， 最终会拼接到SQL中
    private List<Long> deptScopes;

    @Override
    public String getUserType() {
        return USER_TYPE;
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
        this.password = null;
    }

}