package com.wzkris.usercenter.request.oauth2;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 修改 OAuth2 客户端请求体
 */
@Data
@Schema(description = "修改 OAuth2 客户端参数体")
public class OAuth2ClientMngUpdateRequest {

    @NotNull(message = "{invalidParameter.id.invalid}")
    private Long id;

    @NotBlank(message = "{invalidParameter.clientName.invalid}")
    @Schema(description = "客户端名称")
    private String clientName;

    @Schema(description = "客户端状态")
    private String status;

    @NotBlank(message = "{invalidParameter.id.invalid}")
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

