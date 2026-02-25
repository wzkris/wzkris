package com.wzkris.auth.httpclient.token.req;

import jakarta.annotation.Nonnull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OAuth2TokenReq implements Serializable {

    @Nonnull
    private String token;

}