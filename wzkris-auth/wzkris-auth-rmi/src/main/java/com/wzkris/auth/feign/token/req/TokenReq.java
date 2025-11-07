package com.wzkris.auth.feign.token.req;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenReq implements Serializable {

    @NotBlank(message = "authType {validate.notnull}")
    private String authType;

    @NotBlank(message = "token {validate.notnull}")
    private String token;

}
