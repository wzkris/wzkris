package com.wzkris.user.domain.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class OAuth2ClientQueryReq {

    @Schema(description = "客户端id")
    private String clientId;

    @Schema(description = "客户端状态")
    private String status;
}
