package com.wzkris.common.security.oauth2.deserializer;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

import java.io.IOException;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : AuthorizationGrantType 反序列化
 * @date : 2024/08/05 15:32
 */
public class AuthorizationGrantTypeDeserializer extends JsonDeserializer<AuthorizationGrantType> {
    @Override
    public AuthorizationGrantType deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        ObjectNode objectNode = jsonParser.getCodec().readTree(jsonParser);
        return new AuthorizationGrantType(objectNode.get("value").asText());
    }
}
