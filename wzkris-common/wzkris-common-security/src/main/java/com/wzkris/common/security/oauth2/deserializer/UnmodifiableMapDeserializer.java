package com.wzkris.common.security.oauth2.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class UnmodifiableMapDeserializer extends JsonDeserializer<Map<?, ?>> {

    @Override
    public Map<?, ?> deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        ObjectMapper mapper = (ObjectMapper) jp.getCodec();
        JsonNode node = mapper.readTree(jp);

        Map<String, Object> result = new LinkedHashMap<>();
        if (node != null && node.isObject()) {
            Iterable<Map.Entry<String, JsonNode>> fields = node::fields;
            for (Map.Entry<String, JsonNode> field : fields) {
                result.put(field.getKey(), mapper.readValue(field.getValue().traverse(mapper), Object.class));
            }
        }
        return Collections.unmodifiableMap(result);
    }

}