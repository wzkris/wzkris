package com.wzkris.usercenter.request.oauth2;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class OAuth2ClientMngQueryRequest {

    @Schema(description = "客户端id")
    private String clientId;

    @Schema(description = "客户端状态")
    private String status;

}

