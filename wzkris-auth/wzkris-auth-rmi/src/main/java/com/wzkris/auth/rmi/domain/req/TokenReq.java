package com.wzkris.auth.rmi.domain.req;

import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenReq implements Serializable {

    @NotBlank(message = "token {validate.notnull}")
    private String token;
}
