package com.wzkris.common.security.oauth2.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.IOException;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : SimpleGrantedAuthority 反序列化
 * @date : 2024/04/08 13:13
 */
public class SimpleGrantedAuthorityDeserializer extends JsonDeserializer<SimpleGrantedAuthority> {
    @Override
    public SimpleGrantedAuthority deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
//        try {
//            JsonNode node = p.getCodec().readTree(p);
//            if (!node.isObject()) {
//                throw new IllegalArgumentException("Expected a SimpleGrantedAuthority .");
//            }
//
//            if (node.get("role") == null) {
//                return null;
//            }
//            return new SimpleGrantedAuthority(node.get("role").asText());
//        }
//        catch (InvalidTypeIdException e) {
//            SimpleGrantedAuthority grantedAuthority = JsonUtil.parseObject(p, SimpleGrantedAuthority.class);
//            return grantedAuthority;
//        }

        while (!p.isClosed()) {
            JsonToken jsonToken = p.nextToken();
            if (JsonToken.FIELD_NAME.equals(jsonToken)) {
                String fieldName = p.getCurrentName();
                p.nextToken();

                System.out.println(fieldName + "," + p.getValueAsString());
            }
//            System.out.println(jsonToken);
        }

        return null;
    }
}
