package com.thingslink.common.security.oauth2.handler;

import com.thingslink.common.core.utils.StringUtil;
import com.thingslink.common.core.utils.json.JsonUtil;
import com.thingslink.common.security.oauth2.model.LoginAppUser;
import com.thingslink.common.security.oauth2.model.LoginSysUser;
import com.thingslink.common.security.utils.CurrentUserHolder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.user.OAuth2User;
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
import java.util.Map;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 自定义token自省
 * @date : 2024/3/8 14:34.
 * @UPDATE: 2024/5/14 10:11 custom from @org.springframework.security.oauth2.server.resource.introspection.SpringOpaqueTokenIntrospector
 */
public class CustomOpaqueTokenIntrospector implements OpaqueTokenIntrospector {

    private static final ParameterizedTypeReference<Map<String, Object>> STRING_OBJECT_MAP = new ParameterizedTypeReference<Map<String, Object>>() {
    };

    private static final Log log = LogFactory.getLog(CustomOpaqueTokenIntrospector.class);

    private final RestOperations restOperations;

    private final Converter<String, RequestEntity<?>> requestEntityConverter;

    public CustomOpaqueTokenIntrospector(String introspectionUri, String clientId, String clientSecret) {
        Assert.notNull(introspectionUri, "introspectionUri cannot be null");
        Assert.notNull(clientId, "clientId cannot be null");
        Assert.notNull(clientSecret, "clientSecret cannot be null");
        this.requestEntityConverter = this.defaultRequestEntityConverter(URI.create(introspectionUri));
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new ResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse response) throws IOException {
                return false;
            }

            @Override
            public void handleError(ClientHttpResponse response) throws IOException {

            }
        });
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
        if (CurrentUserHolder.isAuthenticated()) {
            return CurrentUserHolder.getPrincipal();
        }

        // 否则校验token
        RequestEntity<?> requestEntity = this.requestEntityConverter.convert(token);
        if (requestEntity == null) {
            throw new OAuth2IntrospectionException("requestEntityConverter returned a null entity");
        }

        ResponseEntity<Map<String, Object>> responseEntity = this.makeRequest(requestEntity);

        Map<String, Object> response = this.adaptToCustomResponse(responseEntity);

        return this.buildOAuth2User(response);
    }

    private ResponseEntity<Map<String, Object>> makeRequest(RequestEntity<?> requestEntity) {
        try {
            return this.restOperations.exchange(requestEntity, STRING_OBJECT_MAP);
        }
        catch (Exception ex) {
            throw new OAuth2IntrospectionException(ex.getMessage(), ex);
        }
    }

    private Map<String, Object> adaptToCustomResponse(ResponseEntity<Map<String, Object>> responseEntity) {
        if (responseEntity.getStatusCode() != HttpStatus.OK || responseEntity.getBody() == null) {
            return Collections.emptyMap();
        }

        return responseEntity.getBody();
    }

    /**
     * 构建登录用户信息
     */
    public OAuth2User buildOAuth2User(Map<String, Object> resMap) {
        if (resMap == null || resMap.get("userType") == null) {
            log.warn("userType is invalid");
            throw new InvalidBearerTokenException(OAuth2ErrorCodes.INVALID_TOKEN);
        }

        String userType = resMap.get("userType").toString();

        if (StringUtil.equals(userType, LoginAppUser.USER_TYPE)) {
            return JsonUtil.parseObject(resMap, LoginAppUser.class);
        }

        if (StringUtil.equals(userType, LoginSysUser.USER_TYPE)) {
            return JsonUtil.parseObject(resMap, LoginSysUser.class);
        }

        throw new InvalidBearerTokenException(OAuth2ErrorCodes.INVALID_TOKEN);
    }

}
