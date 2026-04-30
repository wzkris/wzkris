package com.wzkris.gateway.remote.api.loginuser.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OAuth2TokenQueryRequest implements Serializable {

    private String token;

}

