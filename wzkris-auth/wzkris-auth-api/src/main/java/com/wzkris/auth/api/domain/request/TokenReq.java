package com.wzkris.auth.api.domain.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenReq {

    // token
    @NotBlank(message = "[token] {validate.notnull}")
    private String token;

    // 请求ID
    @NotBlank(message = "[reqId] {validate.notnull}")
    private String reqId;
}
