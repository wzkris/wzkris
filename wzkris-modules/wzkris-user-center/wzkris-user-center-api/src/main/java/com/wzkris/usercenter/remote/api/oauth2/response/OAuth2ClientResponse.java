package com.wzkris.usercenter.remote.api.oauth2.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class OAuth2ClientResponse implements Serializable {

    private Long id;

    private String clientId;

    private String clientSecret;

    private String[] scopes;

    private String[] authorizationGrantTypes;

    private String[] redirectUris;

    private String status;

    private Boolean autoApprove;

}
