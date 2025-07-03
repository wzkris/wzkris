package com.wzkris.auth.security.handler.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wzkris.common.core.domain.Result;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.GenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.oauth2.core.OAuth2TokenIntrospectionClaimNames;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenIntrospection;
import org.springframework.security.oauth2.server.authorization.http.converter.OAuth2TokenIntrospectionHttpMessageConverter;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 返回统一格式
 *
 * @author wzkris
 */
public class CustomOAuth2TokenIntrospectionHttpMessageConverter extends OAuth2TokenIntrospectionHttpMessageConverter {

    private static final ParameterizedTypeReference<Map<String, Object>> STRING_OBJECT_MAP
            = new ParameterizedTypeReference<>() {
    };

    private final GenericHttpMessageConverter<Object> jsonMessageConverter
            = new MappingJackson2HttpMessageConverter(new ObjectMapper());

    private final Converter<OAuth2TokenIntrospection, Map<String, Object>> tokenIntrospectionParametersConverter
            = new OAuth2TokenIntrospectionMapConverter();

    @Override
    protected void writeInternal(OAuth2TokenIntrospection tokenIntrospection, HttpOutputMessage outputMessage)
            throws HttpMessageNotWritableException {
        try {
            Map<String, Object> responseParameters = this.tokenIntrospectionParametersConverter
                    .convert(tokenIntrospection);
            this.jsonMessageConverter.write(Result.ok(responseParameters), STRING_OBJECT_MAP.getType(),
                    MediaType.APPLICATION_JSON, outputMessage);
        } catch (Exception ex) {
            throw new HttpMessageNotWritableException(
                    "An error occurred writing the Token Introspection Response: " + ex.getMessage(), ex);
        }
    }

    private static final class OAuth2TokenIntrospectionMapConverter
            implements Converter<OAuth2TokenIntrospection, Map<String, Object>> {

        @Override
        public Map<String, Object> convert(OAuth2TokenIntrospection source) {
            Map<String, Object> responseClaims = new LinkedHashMap<>(source.getClaims());
            if (!CollectionUtils.isEmpty(source.getScopes())) {
                responseClaims.put(OAuth2TokenIntrospectionClaimNames.SCOPE,
                        StringUtils.collectionToDelimitedString(source.getScopes(), " "));
            }
            if (source.getExpiresAt() != null) {
                responseClaims.put(OAuth2TokenIntrospectionClaimNames.EXP, source.getExpiresAt().getEpochSecond());
            }
            if (source.getIssuedAt() != null) {
                responseClaims.put(OAuth2TokenIntrospectionClaimNames.IAT, source.getIssuedAt().getEpochSecond());
            }
            if (source.getNotBefore() != null) {
                responseClaims.put(OAuth2TokenIntrospectionClaimNames.NBF, source.getNotBefore().getEpochSecond());
            }
            return responseClaims;
        }

    }

}
