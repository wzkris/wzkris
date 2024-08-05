package com.wzkris.common.security.oauth2.deserializer.module;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.wzkris.common.security.oauth2.authentication.WkAuthenticationToken;
import com.wzkris.common.security.oauth2.deserializer.AuthorizationGrantTypeDeserializer;
import com.wzkris.common.security.oauth2.deserializer.GrantedAuthorityDeserializer;
import com.wzkris.common.security.oauth2.deserializer.OAuth2UserDeserializer;
import com.wzkris.common.security.oauth2.deserializer.WkAuthenticationTokenDeserializer;
import com.wzkris.common.security.oauth2.domain.OAuth2User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

import java.io.Serial;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @date : 2024/08/09 16:09
 */
public class OAuth2JacksonModule extends SimpleModule {

    @Serial
    private static final long serialVersionUID = -4741916684501126922L;

    public OAuth2JacksonModule() {
        super(OAuth2JacksonModule.class.getName());
        this.addDeserializer(OAuth2User.class, new OAuth2UserDeserializer());
        this.addDeserializer(WkAuthenticationToken.class, new WkAuthenticationTokenDeserializer());
        this.addDeserializer(AuthorizationGrantType.class, new AuthorizationGrantTypeDeserializer());
        this.addDeserializer(GrantedAuthority.class, new GrantedAuthorityDeserializer());
        this.addDeserializer(GrantedAuthority.class, new GrantedAuthorityDeserializer());
//        this.addDeserializer(SimpleGrantedAuthority.class, new SimpleGrantedAuthorityDeserializer());
    }

//        @Override
//        public void setupModule(SetupContext context) {
//            context.setMixInAnnotations(GrantedAuthority.class, SimpleGrantedAuthorityMixin.class);
//            context.setMixInAnnotations(SimpleGrantedAuthority.class, SimpleGrantedAuthorityMixin.class);
//        }
}
