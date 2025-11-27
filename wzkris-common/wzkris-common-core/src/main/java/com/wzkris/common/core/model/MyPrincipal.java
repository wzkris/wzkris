package com.wzkris.common.core.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.wzkris.common.core.constant.SecurityConstants;
import com.wzkris.common.core.enums.AuthTypeEnum;
import com.wzkris.common.core.model.domain.LoginAdmin;
import com.wzkris.common.core.model.domain.LoginClient;
import com.wzkris.common.core.model.domain.LoginCustomer;
import com.wzkris.common.core.model.domain.LoginTenant;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.Assert;

import java.security.Principal;
import java.util.Set;

/**
 * 核心信息
 *
 * @author wzkris
 */
@Getter
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", include = JsonTypeInfo.As.EXISTING_PROPERTY)
@JsonSubTypes({
        @JsonSubTypes.Type(value = LoginCustomer.class, name = "customer"),
        @JsonSubTypes.Type(value = LoginClient.class, name = "oauth2_client"),
        @JsonSubTypes.Type(value = LoginTenant.class, name = "tenant"),
        @JsonSubTypes.Type(value = LoginAdmin.class, name = "admin")
})
public abstract class MyPrincipal implements Principal {

    private final Long id;

    private final AuthTypeEnum type;

    private final Set<String> permissions;

    @Setter
    private String version = SecurityConstants.DEFAULT_VERSION;

    public MyPrincipal(Long id, AuthTypeEnum type, Set<String> permissions) {
        Assert.notNull(id, "ID cannot be null");
        Assert.notNull(type, "type cannot be null");
        Assert.notNull(permissions, "permissions cannot be null");
        this.id = id;
        this.type = type;
        this.permissions = permissions;
    }

}
