package com.wzkris.common.core.model.domain;

import com.wzkris.common.core.model.CorePrincipal;
import com.wzkris.common.core.enums.AuthType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * @author : wzkris
 * @version : V1.0.1
 * @description : 用户管理
 * @date : 2024/6/14 15:30
 */
@Getter
@Setter
@ToString
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
        super(userId, AuthType.USER.getValue(), permissions);
    }

}
