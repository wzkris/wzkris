package com.wzkris.usercenter.api.oauth2.request;

import com.wzkris.usercenter.enums.oauth2.OAuth2ClientStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "修改 OAuth2 客户端参数体")
public class OAuth2ClientMngUpdateRequest {

    @NotNull(message = "{invalidParameter.id.invalid}")
    private Long id;

    @Schema(description = "客户端名称")
    private String clientName;

    @Schema(description = "客户端状态")
    private OAuth2ClientStatusEnum status;

    @Schema(description = "客户端 id 等价于 app_id")
    private String clientId;

    @Schema(description = "权限域")
    private String[] scopes;

    @Schema(description = "授权类型")
    private String[] authorizationGrantTypes;

    @Schema(description = "回调地址")
    private String[] redirectUris;

    @Schema(description = "放行配置")
    private Boolean autoApprove;

}

