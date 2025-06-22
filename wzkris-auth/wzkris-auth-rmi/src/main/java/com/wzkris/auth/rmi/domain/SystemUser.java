package com.wzkris.auth.rmi.domain;

import com.wzkris.auth.rmi.enums.AuthenticatedType;
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
public class SystemUser extends CorePrincipal {

    private final Long userId;

    private boolean admin;

    private String username;

    private Long tenantId;

    private List<Long> deptScopes;

    public SystemUser() {
        this(null);
    }

    public SystemUser(Long userId) {
        this(userId, Collections.emptySet());
    }

    public SystemUser(Long userId, Set<String> permissions) {
        super(String.valueOf(userId), AuthenticatedType.SYSTEM_USER.getValue(), permissions);
        this.userId = userId;
    }

}
