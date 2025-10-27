package com.wzkris.common.core.model.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wzkris.common.core.enums.AuthType;
import com.wzkris.common.core.model.MyPrincipal;
import lombok.Getter;
import lombok.ToString;

import java.util.Set;

/**
 * @author : wzkris
 * @version : V1.0.1
 * @description : 授权客户端
 * @date : 2024/6/14 15:30
 */
@Getter
@ToString
public class LoginClient extends MyPrincipal {

    private final String clientId;

    @JsonCreator
    public LoginClient(@JsonProperty("id") Long id,
                       @JsonProperty("permissions") Set<String> permissions,
                       @JsonProperty("clientId") String clientId) {
        super(id, AuthType.CLIENT, permissions);
        this.clientId = clientId;
    }

    @Override
    public String getName() {
        return this.clientId;
    }

}
