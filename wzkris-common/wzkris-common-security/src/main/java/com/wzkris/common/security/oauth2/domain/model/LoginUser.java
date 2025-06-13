package com.wzkris.common.security.oauth2.domain.model;

import com.wzkris.common.core.constant.SecurityConstants;
import com.wzkris.common.security.oauth2.domain.AuthBaseUser;
import com.wzkris.common.security.oauth2.enums.LoginType;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * @author : wzkris
 * @version : V1.0.1
 * @description : 登录用户信息
 * @create : 2023/8/7 16:38
 * @update： 2024/12/20 15:50
 */
@Setter
@Getter
public class LoginUser extends AuthBaseUser {

    /**
     * 租户管理员
     */
    private boolean admin;

    /**
     * 登录id
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 租户id
     */
    private Long tenantId;

    /**
     * 部门数据权限， 最终会拼接到SQL中
     */
    private List<Long> deptScopes;

    public LoginUser() {
        this(Collections.emptySet());
    }

    public LoginUser(Set<String> grantedAuthority) {
        super(LoginType.SYSTEM_USER, grantedAuthority);
    }

    @Override
    public String getName() {
        return this.username;
    }

    public boolean getAdmin() {
        return admin;
    }

    public static LoginUser defaultUser() {
        LoginUser user = new LoginUser();
        user.setAdmin(false);
        user.setUserId(SecurityConstants.DEFAULT_USER_ID);
        user.setUsername(SecurityConstants.DEFAULT_USER_NAME);
        user.setTenantId(SecurityConstants.DEFAULT_TENANT_ID);
        return user;
    }

}
