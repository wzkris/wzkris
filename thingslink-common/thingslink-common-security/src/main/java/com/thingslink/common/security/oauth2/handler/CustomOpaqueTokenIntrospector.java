package com.thingslink.common.security.oauth2.handler;

import com.thingslink.common.core.constant.SecurityConstants;
import com.thingslink.common.core.utils.ServletUtil;
import com.thingslink.common.core.utils.json.JsonUtil;
import com.thingslink.common.security.oauth2.domain.OAuth2User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.security.oauth2.server.resource.introspection.OAuth2IntrospectionException;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.List;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 自定义token自省
 * @date : 2024/3/8 14:34.
 * @UPDATE: 2024/5/14 10:11 custom from @org.springframework.security.oauth2.server.resource.introspection.SpringOpaqueTokenIntrospector
 */
@Slf4j
public class CustomOpaqueTokenIntrospector implements OpaqueTokenIntrospector {

    private static final ParameterizedTypeReference<OAuth2User> STRING_OBJECT_MAP = new ParameterizedTypeReference<>() {
    };

    private final RestOperations restOperations;

    private final Converter<String, RequestEntity<?>> requestEntityConverter;

    public CustomOpaqueTokenIntrospector(String introspectionUri, String clientId, String clientSecret) {
        Assert.notNull(introspectionUri, "introspectionUri cannot be null");
        Assert.notNull(clientId, "clientId cannot be null");
        Assert.notNull(clientSecret, "clientSecret cannot be null");
        this.requestEntityConverter = this.defaultRequestEntityConverter(URI.create(introspectionUri));
        RestTemplate restTemplate = this.defaultRestTemplate(new RestTemplate());
        restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor(clientId, clientSecret));
        this.restOperations = restTemplate;
    }

    private Converter<String, RequestEntity<?>> defaultRequestEntityConverter(URI introspectionUri) {
        return (token) -> {
            HttpHeaders headers = requestHeaders();
            MultiValueMap<String, String> body = requestBody(token);
            return new RequestEntity<>(body, headers, HttpMethod.POST, introspectionUri);
        };
    }

    private RestTemplate defaultRestTemplate(RestTemplate restTemplate) {
        // 替换jackson序列化mapper
        List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();
        for (HttpMessageConverter<?> messageConverter : messageConverters) {
            if (messageConverter instanceof MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter) {
                mappingJackson2HttpMessageConverter.setObjectMapper(JsonUtil.getObjectMapper());
            }
        }
        restTemplate.setErrorHandler(new ResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse response) throws IOException {
                return false;
            }

            @Override
            public void handleError(ClientHttpResponse response) throws IOException {

            }
        });
        return restTemplate;
    }

    private HttpHeaders requestHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return headers;
    }

    private MultiValueMap<String, String> requestBody(String token) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("token", token);
        return body;
    }

    @Override
    public OAuth2AuthenticatedPrincipal introspect(String token) {
        // request不会为空
        HttpServletRequest request = ServletUtil.getRequest();
        if (request.getHeader(SecurityConstants.PRINCIPAL_HEADER) != null) {
            return JsonUtil.parseObject(request.getHeader(SecurityConstants.PRINCIPAL_HEADER), OAuth2User.class);
        }

        // 否则校验token, 此处token可能为用户token或应用token
        RequestEntity<?> requestEntity = this.requestEntityConverter.convert(token);
        if (requestEntity == null) {
            throw new OAuth2IntrospectionException("requestEntityConverter returned a null entity");
        }

        ResponseEntity<OAuth2User> responseEntity = this.makeRequest(requestEntity);

        return this.adaptToCustomResponse(responseEntity);
    }

    private ResponseEntity<OAuth2User> makeRequest(RequestEntity<?> requestEntity) {
        try {
            return this.restOperations.exchange(requestEntity, STRING_OBJECT_MAP);
        }
        catch (Exception ex) {
            throw new OAuth2IntrospectionException(ex.getMessage(), ex);
        }
    }

    private OAuth2User adaptToCustomResponse(ResponseEntity<OAuth2User> responseEntity) {
        if (responseEntity.getStatusCode() != HttpStatus.OK || responseEntity.getBody() == null) {
            throw new InvalidBearerTokenException(OAuth2ErrorCodes.INVALID_TOKEN);
        }

        OAuth2User oAuth2User = responseEntity.getBody();

        if (oAuth2User.getOauth2Type() == null) {
            throw new InvalidBearerTokenException(OAuth2ErrorCodes.INVALID_TOKEN);
        }

        return oAuth2User;
    }

}
