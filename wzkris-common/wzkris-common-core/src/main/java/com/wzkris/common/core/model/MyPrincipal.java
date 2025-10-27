package com.wzkris.common.core.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.wzkris.common.core.enums.AuthType;
import com.wzkris.common.core.model.domain.LoginClient;
import com.wzkris.common.core.model.domain.LoginCustomer;
import com.wzkris.common.core.model.domain.LoginStaff;
import com.wzkris.common.core.model.domain.LoginUser;
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
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", include = JsonTypeInfo.As.EXISTING_PROPERTY)
@JsonSubTypes({
        @JsonSubTypes.Type(value = LoginCustomer.class, name = "customer"),
        @JsonSubTypes.Type(value = LoginClient.class, name = "oauth2_client"),
        @JsonSubTypes.Type(value = LoginStaff.class, name = "staff"),
        @JsonSubTypes.Type(value = LoginUser.class, name = "user")
})
public abstract class MyPrincipal implements Principal {

    private final Long id;

    private final AuthType type;

    private final Set<String> permissions;

    public MyPrincipal(Long id, AuthType type, Set<String> permissions) {
        Assert.notNull(id, "ID cannot be null");
        Assert.notNull(type, "type cannot be null");
        Assert.notNull(permissions, "permissions cannot be null");
        this.id = id;
        this.type = type;
        this.permissions = permissions;
    }

}
