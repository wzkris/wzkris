package com.wzkris.auth.feign.domain;

import com.wzkris.auth.feign.enums.AuthenticatedType;
import com.wzkris.common.core.domain.CorePrincipal;
import lombok.Getter;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Collections;
import java.util.Set;

/**
 * @author : wzkris
 * @version : V1.0.1
 * @description : 授权客户端
 * @date : 2024/6/14 15:30
 */
@Getter
public class AuthedClient extends CorePrincipal {

    private final String clientId;

    public AuthedClient() {
        this(null, Collections.emptySet());
    }

    public AuthedClient(String clientId, Set<String> scopes) {
        super((long) HashCodeBuilder.reflectionHashCode(clientId), AuthenticatedType.CLIENT.getValue(), scopes);
        this.clientId = clientId;
    }

}
