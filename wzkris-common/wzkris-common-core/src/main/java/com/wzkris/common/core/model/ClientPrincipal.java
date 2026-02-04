package com.wzkris.common.core.model;

import lombok.Data;

import java.security.Principal;

@Data
public class ClientPrincipal implements Principal {

    private String clientId;

    @Override
    public String getName() {
        return clientId;
    }

}
