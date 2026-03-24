package com.wzkris.gateway.httpclient.loginuser.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OAuth2TokenQueryReq implements Serializable {

    private String token;

}
