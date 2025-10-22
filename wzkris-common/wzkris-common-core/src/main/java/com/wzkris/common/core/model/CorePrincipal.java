package com.wzkris.common.core.model;

import lombok.Getter;

import java.security.Principal;
import java.util.Set;

/**
 * 用户核心信息
 *
 * @author wzkris
 */
@Getter
public abstract class CorePrincipal implements Principal {

    private final Long id;

    private final String type;

    private final Set<String> permissions;

    public CorePrincipal(Long id, String type, Set<String> permissions) {
        this.id = id;
        this.type = type;
        this.permissions = permissions;
    }

}
