package com.wzkris.common.security.oauth2.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.security.core.GrantedAuthority;

import java.io.IOException;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : SimpleGrantedAuthority 反序列化
 * @date : 2024/04/08 13:13
 */
public class GrantedAuthorityDeserializer extends JsonDeserializer<GrantedAuthority> {
    @Override
    public GrantedAuthority deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);

        return null;
    }
}
