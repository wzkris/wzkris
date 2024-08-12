package com.wzkris.common.security.oauth2.deserializer.module;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.wzkris.common.security.oauth2.deserializer.GrantedAuthorityDeserializer;
import com.wzkris.common.security.oauth2.deserializer.SimpleGrantedAuthorityDeserializer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.Serial;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 跨微服务OAuth2对象反序列化支持
 * @date : 2024/08/09 16:09
 */
public class OAuth2JacksonModule extends SimpleModule {

    @Serial
    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    public OAuth2JacksonModule() {
        super(OAuth2JacksonModule.class.getName());
        this.addDeserializer(GrantedAuthority.class, new GrantedAuthorityDeserializer());
        this.addDeserializer(SimpleGrantedAuthority.class, new SimpleGrantedAuthorityDeserializer());
    }
}
