package com.wzkris.common.security.oauth2.domain.model;

import com.wzkris.common.security.oauth2.domain.AuthBaseUser;
import com.wzkris.common.security.oauth2.enums.LoginType;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.Set;


/**
 * @author : wzkris
 * @version : V1.0.1
 * @description : 客户端用户
 * @date : 2023/8/7 16:38
 * @UPDATE： 2024/4/9 09:29
 */
@Setter
@Getter
public class ClientUser extends AuthBaseUser {

    /**
     * 登录id
     */
    private Long userId;

    /**
     * 手机号
     */
    private String phoneNumber;

    public ClientUser() {
        this(Collections.emptySet());
    }

    public ClientUser(Set<String> grantedAuthority) {
        super(LoginType.CLIENT_USER, grantedAuthority);
    }

    @Override
    public String getName() {
        return this.phoneNumber;
    }

}
