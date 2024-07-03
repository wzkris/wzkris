package com.wzkris.user.api.domain.dto;

import lombok.Data;

import java.util.List;

/**
 * OAuth2 客户端传输层
 *
 * @author wzkris
 */
@Data
public class OAuth2ClientDTO {
    // 客户端id 等价于app_id
    private String clientId;
    // 客户端密钥 等价于app_secret
    private String clientSecret;
    // 权限域
    private List<String> scopes;
    // 授权类型
    private List<String> authorizationGrantTypes;
    // 回调地址
    private List<String> redirectUris;
    // 客户端状态
    private String status;
    // 是否自动放行
    private Boolean autoApprove;
}
