package com.wzkris.auth.httpservice.token.req;

import jakarta.annotation.Nonnull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginUserReq implements Serializable {

    @Nonnull
    private String authType;

    @Nonnull
    private Long uid;

    @Nonnull
    private String sid;

}
