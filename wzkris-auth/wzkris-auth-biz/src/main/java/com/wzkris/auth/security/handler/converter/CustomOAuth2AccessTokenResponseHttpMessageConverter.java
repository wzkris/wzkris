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
import org.springframework.security.oauth2.core.endpoint.DefaultOAuth2AccessTokenResponseMapConverter;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;

import java.util.Map;

/**
 * 返回统一格式
 *
 * @author wzkris
 */
public class CustomOAuth2AccessTokenResponseHttpMessageConverter extends OAuth2AccessTokenResponseHttpMessageConverter {

    private final ParameterizedTypeReference<Map<String, Object>> STRING_OBJECT_MAP
            = new ParameterizedTypeReference<>() {
    };

    private final Converter<OAuth2AccessTokenResponse, Map<String, Object>> accessTokenResponseParametersConverter
            = new DefaultOAuth2AccessTokenResponseMapConverter();

    private final GenericHttpMessageConverter<Object> jsonMessageConverter
            = new MappingJackson2HttpMessageConverter(new ObjectMapper());

    @Override
    public void writeInternal(OAuth2AccessTokenResponse tokenResponse, HttpOutputMessage outputMessage)
            throws HttpMessageNotWritableException {
        try {
            // 获得token
            Map<String, Object> tokenData = this.accessTokenResponseParametersConverter.convert(tokenResponse);
            jsonMessageConverter.write(
                    Result.ok(tokenData), STRING_OBJECT_MAP.getType(), MediaType.APPLICATION_JSON, outputMessage);
        } catch (Exception ex) {
            throw new HttpMessageNotWritableException(
                    "An error occurred writing the OAuth 2.0 Access Token Response: " + ex.getMessage(), ex);
        }
    }

}
