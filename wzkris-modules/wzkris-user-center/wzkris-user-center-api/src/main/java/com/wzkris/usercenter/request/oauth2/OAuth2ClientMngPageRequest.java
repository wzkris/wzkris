package com.wzkris.usercenter.request.oauth2;

import com.wzkris.usercenter.enums.oauth2.OAuth2ClientStatusEnum;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "OAuth2 客户端分页查询参数体")
public class OAuth2ClientMngPageRequest {

    @Parameter(description = "客户端id")
    private String clientId;

    @Parameter(description = "客户端状态")
    private OAuth2ClientStatusEnum status;
}
