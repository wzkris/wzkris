package com.wzkris.common.core.model;

import lombok.Getter;
import org.springframework.util.Assert;

import java.security.Principal;
import java.util.Set;

/**
 * 核心信息
 *
 * @author wzkris
 */
@Getter
public abstract class MyPrincipal implements Principal {

    private final Long id;

    private final String type;

    private final Set<String> permissions;

    public MyPrincipal(Long id, String type, Set<String> permissions) {
        Assert.notNull(id, "ID cannot be null");
        Assert.notNull(type, "type cannot be null");
        Assert.notNull(permissions, "permissions cannot be null");
        this.id = id;
        this.type = type;
        this.permissions = permissions;
    }

}
