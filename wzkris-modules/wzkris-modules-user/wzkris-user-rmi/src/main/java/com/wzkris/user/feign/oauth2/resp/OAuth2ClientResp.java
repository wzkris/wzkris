package com.wzkris.user.feign.oauth2.resp;

import lombok.Data;

import java.io.Serializable;

/**
 * OAuth2 客户端传输层
 *
 * @author wzkris
 */
@Data
public class OAuth2ClientResp implements Serializable {

    // 客户端id 等价于app_id
    private String clientId;

    // 客户端密钥 等价于app_secret
    private String clientSecret;

    // 权限域
    private String[] scopes;

    // 授权类型
    private String[] authorizationGrantTypes;

    // 回调地址
    private String[] redirectUris;

    // 客户端状态
    private String status;

    // 是否自动放行
    private Boolean autoApprove;

}
