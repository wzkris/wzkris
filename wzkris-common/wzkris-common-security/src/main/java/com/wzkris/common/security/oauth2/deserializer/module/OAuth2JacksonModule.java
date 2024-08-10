package com.wzkris.common.security.oauth2.deserializer.module;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.wzkris.common.security.oauth2.authentication.WkAuthenticationToken;
import com.wzkris.common.security.oauth2.deserializer.SimpleGrantedAuthorityDeserializer;
import com.wzkris.common.security.oauth2.deserializer.UnmodifiableMapDeserializer;
import com.wzkris.common.security.oauth2.deserializer.WkAuthenticationTokenDeserializer;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.Serial;
import java.util.Map;

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
        this.addDeserializer(Map.class, new UnmodifiableMapDeserializer());
        this.addDeserializer(WkAuthenticationToken.class, new WkAuthenticationTokenDeserializer());
        this.addDeserializer(SimpleGrantedAuthority.class, new SimpleGrantedAuthorityDeserializer());
    }

}
