package com.thingslink.user.api.domain.dto;

import lombok.Data;

import java.util.List;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : OAuth2客户端
 * @date : 2023/6/2 16:31
 */
@Data
public class Oauth2ClientDTO {
    private Long id;
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

    public enum Status {
        NORMAL("0", "正常"),
        DISABLE("1", "停用"),
        WAIT_AUDIT("2", "待审核"),
        REJECT_AUDIT("3", "审核拒绝");


        private final String value;

        Status(String value, String description) {
            this.value = value;
        }

        public String value() {
            return this.value;
        }
    }
}