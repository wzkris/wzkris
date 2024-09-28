package com.wzkris.common.security.oauth2.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wzkris.auth.api.RemoteTokenApi;
import com.wzkris.auth.api.domain.ReqToken;
import com.wzkris.common.core.constant.SecurityConstants;
import com.wzkris.common.core.domain.Result;
import com.wzkris.common.core.exception.BusinessException;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.security.oauth2.deserializer.module.OAuth2JacksonModule;
import com.wzkris.common.security.oauth2.domain.OAuth2User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.server.resource.BearerTokenErrors;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Map;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 自定义token自省
 * @date : 2024/3/8 14:34.
 * @UPDATE: 2024/5/14 10:11 custom from @org.springframework.security.oauth2.server.resource.introspection.SpringOpaqueTokenIntrospector
 */
@Slf4j
public final class CustomOpaqueTokenIntrospector implements OpaqueTokenIntrospector {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final RemoteTokenApi remoteTokenApi;

    public CustomOpaqueTokenIntrospector(RemoteTokenApi remoteTokenApi) {
        this.remoteTokenApi = remoteTokenApi;
        objectMapper.registerModules(new OAuth2JacksonModule());
    }

    @Override
    public OAuth2AuthenticatedPrincipal introspect(String token) {
        // request不会为空
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String reqId = request.getHeader(SecurityConstants.TOKEN_REQ_ID_HEADER);
        if (StringUtil.isBlank(reqId)) {
            throw new OAuth2AuthenticationException(BearerTokenErrors.invalidRequest("invalid request id"));
        }

        try {
            Result<Map<String, Object>> tokenResult = remoteTokenApi.checkToken(new ReqToken(token, reqId));
            Map<String, Object> map = tokenResult.checkData();

            return this.adaptToCustomResponse(map);
        }
        catch (BusinessException e) {
            throw new InvalidBearerTokenException(e.getMessage());
        }
        catch (Exception e) {
            log.error("token校验发生异常:{}", e.getMessage(), e);
            throw new BusinessException(e.getMessage());
        }
    }

    private OAuth2User adaptToCustomResponse(Map<String, Object> responseEntity) {
        return objectMapper.convertValue(responseEntity, OAuth2User.class);
    }

}
