package com.wzkris.usercenter.api.oauth2.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wzkris.usercenter.enums.oauth2.OAuth2ClientStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * OAuth2客户端详情响应（Mng 轨单对象 -> {域}MngQueryResponse）
 *
 * @author wzkris
 */
@Data
@NoArgsConstructor
public class OAuth2ClientMngQueryResponse {

    private Long id;

    @Schema(description = "客户端名称")
    private String clientName;

    @Schema(description = "客户端状态")
    private OAuth2ClientStatusEnum status;

    @Schema(description = "客户端id 等价于app_id")
    private String clientId;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Schema(description = "客户端密钥 等价于app_secret")
    private String clientSecret;

    @Schema(description = "权限域")
    private String[] scopes;

    @Schema(description = "授权类型")
    private String[] authorizationGrantTypes;

    @Schema(description = "回调地址")
    private String[] redirectUris;

    @Schema(description = "放行配置")
    private Boolean autoApprove;

}
