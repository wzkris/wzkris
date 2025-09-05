package com.wzkris.auth.feign.domain;

import com.wzkris.auth.feign.enums.AuthenticatedType;
import com.wzkris.common.core.domain.CorePrincipal;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * @author : wzkris
 * @version : V1.0.1
 * @description : 系统用户
 * @date : 2024/6/14 15:30
 */
@Getter
@Setter
public class LoginUser extends CorePrincipal {

    private boolean admin;

    private String username;

    private Long tenantId;

    private List<Long> deptScopes;

    public LoginUser() {
        this(null);
    }

    public LoginUser(Long userId) {
        this(userId, Collections.emptySet());
    }

    public LoginUser(Long userId, Set<String> permissions) {
        super(userId, AuthenticatedType.SYSTEM_USER.getValue(), permissions);
    }

}
