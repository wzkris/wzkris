package com.wzkris.common.security.serialize;

import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;

public class OAuth2CustomModule extends SimpleModule {

    public OAuth2CustomModule() {
        super(OAuth2CustomModule.class.getName());
    }

    @Override
    public void setupModule(SetupContext context) {
        context.setMixInAnnotations(BearerTokenAuthentication.class, BearerTokenAuthenticationMixin.class);
        context.setMixInAnnotations(OAuth2ClientAuthenticationToken.class, OAuth2ClientAuthenticationTokenMixin.class);
        context.setMixInAnnotations(ClientAuthenticationMethod.class, ClientAuthenticationMethodMixin.class);
        context.setMixInAnnotations(RegisteredClient.class, RegisteredClientMixin.class);
        context.setMixInAnnotations(AuthorizationGrantType.class, AuthorizationGrantTypeMixin.class);
        context.setMixInAnnotations(ClientSettings.class, ClientSettingsMixin.class);
        context.setMixInAnnotations(TokenSettings.class, TokenSettingsMixin.class);
    }
}
