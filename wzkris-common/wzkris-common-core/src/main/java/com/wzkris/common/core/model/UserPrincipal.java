package com.wzkris.common.core.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.wzkris.common.core.enums.AuthTypeEnum;
import com.wzkris.common.core.model.domain.LoginAdmin;
import com.wzkris.common.core.model.domain.LoginClient;
import com.wzkris.common.core.model.domain.LoginCustomer;
import com.wzkris.common.core.model.domain.LoginTenant;
import com.wzkris.common.core.utils.StringUtil;
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
public abstract class UserPrincipal implements Principal {

    private final Long id;

    private final String type;

    private final Set<String> perms;

    @Setter
    private String hint = StringUtil.EMPTY;

    public UserPrincipal(Long id, AuthTypeEnum authType, Set<String> perms) {
        Assert.notNull(id, "ID cannot be null");
        Assert.notNull(authType, "authType cannot be null");
        Assert.notNull(perms, "permissions cannot be null");
        this.id = id;
        this.type = authType.getValue();
        this.perms = perms;
    }

}
