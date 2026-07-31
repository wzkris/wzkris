package com.wzkris.usercenter.remote.api.oauth2.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class OAuth2ClientQueryRequest implements Serializable {

    @Schema(description = "id")
    private String id;

    @Schema(description = "客户端id")
    private String clientId;

}
