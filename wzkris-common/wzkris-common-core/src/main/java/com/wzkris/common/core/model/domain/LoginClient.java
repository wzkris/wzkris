package com.wzkris.common.core.model.domain;

import com.wzkris.common.core.model.CorePrincipal;
import com.wzkris.common.core.enums.AuthType;
import lombok.Getter;
import lombok.ToString;

import java.util.Collections;
import java.util.Set;

/**
 * @author : wzkris
 * @version : V1.0.1
 * @description : 授权客户端
 * @date : 2024/6/14 15:30
 */
@Getter
@ToString
public class LoginClient extends CorePrincipal {

    private final String clientId;

    public LoginClient() {
        this(null, Collections.emptySet());
    }

    public LoginClient(String clientId, Set<String> scopes) {
        super(System.currentTimeMillis(), AuthType.CLIENT.getValue(), scopes);
        this.clientId = clientId;
    }

}
