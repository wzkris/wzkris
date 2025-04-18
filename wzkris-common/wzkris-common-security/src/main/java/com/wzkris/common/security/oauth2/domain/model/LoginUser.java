package com.wzkris.common.security.oauth2.domain.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    public LoginUser(String id) {
        this(id, Collections.emptySet());
    }

    @JsonCreator
    public LoginUser(@JsonProperty("id") String id, @JsonProperty("grantedAuthority") Set<String> grantedAuthority) {
        super(id, LoginType.SYSTEM_USER, grantedAuthority);
    }

    @Override
    public String getName() {
        return this.username;
    }

    public boolean getAdmin() {
        return admin;
    }

}