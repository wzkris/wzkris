package com.wzkris.auth.httpservice.token.req;

import jakarta.annotation.Nonnull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenReq implements Serializable {

    @Nonnull
    private String authType;

    @Nonnull
    private String token;

}
