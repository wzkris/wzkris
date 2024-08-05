package com.wzkris.common.security.oauth2.deserializer;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wzkris.common.security.oauth2.authentication.WkAuthenticationToken;

import java.io.IOException;

public class WkAuthenticationTokenDeserializer extends JsonDeserializer<WkAuthenticationToken> {
    @Override
    public WkAuthenticationToken deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JacksonException {
        ObjectMapper mapper = (ObjectMapper) jp.getCodec();
        JsonNode jsonNode = mapper.readTree(jp);
        return null;
    }
}
