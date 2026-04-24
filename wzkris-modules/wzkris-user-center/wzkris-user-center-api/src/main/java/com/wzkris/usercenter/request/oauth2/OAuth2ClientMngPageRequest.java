package com.wzkris.usercenter.request.oauth2;

import com.wzkris.usercenter.enums.oauth2.OAuth2ClientStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class OAuth2ClientMngPageRequest {

    @Schema(description = "客户端id")
    private String clientId;

    @Schema(description = "客户端状态")
    private OAuth2ClientStatusEnum status;

}

