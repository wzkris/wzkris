package com.wzkris.auth.rmi.domain.req;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenReq implements Serializable {

    @NotBlank(message = "token {validate.notnull}")
    private String token;

}
