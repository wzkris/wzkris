package com.wzkris.usercenter.remote.api.oauth2.response;

import com.wzkris.usercenter.enums.oauth2.OAuth2ClientStatusEnum;
import lombok.Data;

import java.io.Serializable;

@Data
public class OAuth2ClientListResponse implements Serializable {

    private Long id;

    private String clientId;

    private String clientSecret;

    private String[] scopes;

    private String[] authorizationGrantTypes;

    private String[] redirectUris;

    private OAuth2ClientStatusEnum status;

    private Boolean autoApprove;

}
