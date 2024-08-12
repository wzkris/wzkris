package com.wzkris.common.security.oauth2.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.MissingNode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.IOException;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : GrantedAuthority 反序列化, 包含其所有子类；这里只解析了 SimpleGrantedAuthority
 * @date : 2024/08/10 14:44
 */
public class GrantedAuthorityDeserializer extends JsonDeserializer<GrantedAuthority> {
    @Override
    public GrantedAuthority deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        JsonNode node = jp.getCodec().readTree(jp);

        JsonNode authority = this.readJsonNode(node, "authority");

        if (authority.isTextual()) {
            return new SimpleGrantedAuthority(authority.asText());
        }

        return null;
    }

    private JsonNode readJsonNode(JsonNode jsonNode, String field) {
        return jsonNode.has(field) ? jsonNode.get(field) : MissingNode.getInstance();
    }
}
