package com.wzkris.common.security.oauth2.deserializer;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.wzkris.common.core.utils.json.JsonUtil;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;

import java.io.IOException;
import java.util.Map;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : GrantedAuthority 反序列化
 * @date : 2024/04/08 13:13
 */
public class GrantedAuthorityDeserializer extends JsonDeserializer<GrantedAuthority> {
    @Override
    public GrantedAuthority deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        JsonNode node = p.getCodec().readTree(p);

        // NOTICE: @class GrantedAuthority 有众多实现类，由于只用到了SimpleGrantedAuthority，所以不做多余的处理
        if (node.get("authority") != null) {
            if (node.get("attributes") != null) {
                return new OAuth2UserAuthority(node.get("authority").asText(), JsonUtil.parseObject(node.get("attributes").asText(), Map.class));
            }
            else {
                return new SimpleGrantedAuthority(node.get("authority").asText());
            }
        }
        return null;
    }
}
