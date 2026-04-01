package com.wzkris.auth.remote.api.loginuser.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OAuth2TokenQueryRequest implements Serializable {

    @NotBlank(message = "token不能为空")
    private String token;

}


