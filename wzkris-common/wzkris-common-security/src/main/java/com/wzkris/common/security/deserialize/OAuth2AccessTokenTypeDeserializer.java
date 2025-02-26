package com.wzkris.common.security.deserialize;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.OAuth2AccessToken;

import java.io.IOException;

/**
 * 该类为了解决#OAuth2AccessToken.TokenType mixin反序列化后与常量池中OAuth2AccessToken.TokenType.BEARER内存地址不一致的问题
 *
 * @author wzkris
 * @date 2025/03/02
 */
@Slf4j
public class OAuth2AccessTokenTypeDeserializer extends JsonDeserializer<OAuth2AccessToken.TokenType> {

    @Override
    public OAuth2AccessToken.TokenType deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        JsonNode node = p.getCodec().readTree(p);
        if (!OAuth2AccessToken.TokenType.BEARER.getValue().equals(node.get("value").asText())) {
            log.warn("Invalid token type: {}, but will ignore it", node.get("value").asText());
            return null;
        }
        return OAuth2AccessToken.TokenType.BEARER;
    }
}
