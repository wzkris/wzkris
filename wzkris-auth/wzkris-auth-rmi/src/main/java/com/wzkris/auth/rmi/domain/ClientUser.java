package com.wzkris.auth.rmi.domain;

import com.wzkris.auth.rmi.enums.AuthenticatedType;
import com.wzkris.common.core.domain.CorePrincipal;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.Set;

/**
 * @author : wzkris
 * @version : V1.0.1
 * @description : 客户端用户
 * @date : 2024/6/14 15:30
 */
@Setter
@Getter
public class ClientUser extends CorePrincipal {

    /**
     * 登录id
     */
    private final Long userId;

    /**
     * 手机号
     */
    private String phoneNumber;

    public ClientUser() {
        this(null);
    }

    public ClientUser(Long userId) {
        this(userId, Collections.emptySet());
    }

    public ClientUser(Long userId, Set<String> permissions) {
        super(userId, AuthenticatedType.CLIENT_USER.getValue(), permissions);
        this.userId = userId;
    }

}
