package com.wzkris.common.core.domain;

import lombok.Getter;

import java.io.Serializable;
import java.security.Principal;
import java.util.Set;

/**
 * 用户核心信息
 *
 * @author wzkris
 */
@Getter
public class CorePrincipal implements Principal {

    private final Serializable id;

    private final String type;

    private final Set<String> permissions;

    public CorePrincipal(Serializable id, String type, Set<String> permissions) {
        this.id = id;
        this.type = type;
        this.permissions = permissions;
    }

    @Override
    public final String getName() {
        return this.id.toString();
    }

}
