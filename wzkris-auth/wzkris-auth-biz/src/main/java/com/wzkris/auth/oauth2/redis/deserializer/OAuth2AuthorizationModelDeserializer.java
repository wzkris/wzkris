package com.wzkris.auth.oauth2.redis.deserializer;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.MissingNode;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.wzkris.auth.oauth2.model.OAuth2AuthorizationModel;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class OAuth2AuthorizationModelDeserializer extends JsonDeserializer<OAuth2AuthorizationModel> {

    private static final TypeReference<Set<String>> AUTHORIZED_SCOPES = new TypeReference<>() {
    };

    @Override
    public OAuth2AuthorizationModel deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JacksonException {
        ObjectMapper mapper = (ObjectMapper) jp.getCodec();
        JsonNode jsonNode = mapper.readTree(jp);

        String id = this.readJsonNode(jsonNode, OAuth2AuthorizationModel.Fields.id).asText();
        String principalName = this.readJsonNode(jsonNode, OAuth2AuthorizationModel.Fields.principalName).asText();
        String registeredClientId = this.readJsonNode(jsonNode, OAuth2AuthorizationModel.Fields.registeredClientId).asText();
        String authorizationGrantType = this.readJsonNode(jsonNode, OAuth2AuthorizationModel.Fields.authorizationGrantType).asText();
        Set<String> scopes = this.getAuthorizedScopes(mapper, this.readJsonNode(jsonNode, OAuth2AuthorizationModel.Fields.authorizedScopes));
        Map<String, Object> attributes = this.getAttributes(mapper, this.readJsonNode(jsonNode, OAuth2AuthorizationModel.Fields.attributes));


        return new OAuth2AuthorizationModel()
                .setId(id)
                .setPrincipalName(principalName)
                .setRegisteredClientId(registeredClientId)
                .setAuthorizationGrantType(authorizationGrantType)
                .setAuthorizedScopes(scopes)
                .setAttributes(attributes);
    }

    private Map<String, Object> getAttributes(ObjectMapper mapper, JsonNode attrNode) throws IOException {
        Map<String, Object> attributes = mapper.readValue(attrNode.traverse(), TypeFactory.defaultInstance().constructMapType(Map.class, String.class, Object.class));
        attributes.remove("@class");
        return attributes;
    }

    private Set<String> getAuthorizedScopes(ObjectMapper objectMapper, JsonNode scopeNode) throws IOException {
        if (scopeNode.isArray()) {
            return objectMapper.readValue(scopeNode.get(1).traverse(), TypeFactory.defaultInstance().constructCollectionType(Set.class, String.class));
        }
        return new HashSet<>();
    }

    private JsonNode readJsonNode(JsonNode jsonNode, String field) {
        return jsonNode.has(field) ? jsonNode.get(field) : MissingNode.getInstance();
    }
}
