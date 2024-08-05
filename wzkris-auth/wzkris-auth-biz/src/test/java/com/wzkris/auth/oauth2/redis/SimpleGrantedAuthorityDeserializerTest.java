package com.wzkris.auth.oauth2.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.wzkris.common.security.oauth2.deserializer.AuthorizationGrantTypeDeserializer;
import com.wzkris.common.security.oauth2.deserializer.SimpleGrantedAuthorityDeserializer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

@DisplayName("SimpleGrantedAuthority反序列化测试")
public class SimpleGrantedAuthorityDeserializerTest {

    static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addDeserializer(AuthorizationGrantType.class, new AuthorizationGrantTypeDeserializer());
        simpleModule.addDeserializer(SimpleGrantedAuthority.class, new SimpleGrantedAuthorityDeserializer());
        objectMapper.registerModules(simpleModule);
    }

    @Test
    public void test() throws JsonProcessingException {
        String STR = """
                {
                   "@class": "org.springframework.security.core.authority.SimpleGrantedAuthority",
                   "role": "*"
                 }""";
        SimpleGrantedAuthority grantedAuthority = objectMapper.readValue(STR, SimpleGrantedAuthority.class);
        System.out.println(grantedAuthority);
    }
}
