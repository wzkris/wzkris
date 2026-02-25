package com.wzkris.gateway.filter;

import com.wzkris.common.core.constant.CustomHeaderConstants;
import com.wzkris.common.core.enums.BizBaseCodeEnum;
import com.wzkris.common.core.exception.service.ResultException;
import com.wzkris.common.core.model.ClientPrincipal;
import com.wzkris.common.core.model.LoginUser;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.JsonUtil;
import com.wzkris.gateway.properties.PermitAllProperties;
import com.wzkris.gateway.service.TokenExtractionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.function.HandlerFilterFunction;
import org.springframework.web.servlet.function.HandlerFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import java.util.Collection;
import java.util.Set;

/**
 * 基于 Gateway MVC 原生 {@link HandlerFilterFunction} 的统一认证过滤器。
 * 在网关转发链内执行：黑/白名单判断、Token 认证、将身份/权限以请求头形式带入下游请求。
 *
 * @author wzkris
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationFilter implements HandlerFilterFunction<ServerResponse, ServerResponse> {

    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    private final TokenExtractionService tokenExtractionService;

    private final PermitAllProperties permitAllProperties;

    private final Set<String> permitAllAnnotations;

    private static ServerResponse jsonResponse(HttpStatus status, Object body) {
        return ServerResponse.status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .body(body);
    }

    private static boolean isPathPermitted(Collection<String> patterns, String path) {
        return patterns != null && patterns.stream()
                .anyMatch(pattern -> PATH_MATCHER.match(pattern, path));
    }

    @Override
    public ServerResponse filter(ServerRequest request, HandlerFunction<ServerResponse> next) throws Exception {
        String path = request.path();

        if (isPathDenied(path)) {
            return jsonResponse(HttpStatus.FORBIDDEN,
                    Result.init(BizBaseCodeEnum.ACCESS_DENIED.value(), null, BizBaseCodeEnum.ACCESS_DENIED.desc()));
        }

        if (isPermitAllPath(path)) {
            return next.handle(request);
        }

        try {
            Authentication authentication = tokenExtractionService.getAuthentication(request.servletRequest());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            ServerRequest requestWithHeaders = addAuthHeaders(request, authentication);
            return next.handle(requestWithHeaders);
        } catch (ResultException ex) {
            return jsonResponse(HttpStatus.valueOf(ex.getHttpStatusCode()), ex.getResult());
        } catch (Exception ex) {
            log.error("AuthenticationHandlerFilterFunction error", ex);
            return jsonResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                    Result.init(BizBaseCodeEnum.SYSTEM_ERROR.value(), null, ex.getMessage()));
        } finally {
            SecurityContextHolder.clearContext();
        }
    }

    private ServerRequest addAuthHeaders(ServerRequest request, Authentication authentication) {
        return ServerRequest.from(request)
                .headers(h -> {
                    Object principal = authentication.getPrincipal();
                    if (principal instanceof ClientPrincipal) {
                        h.set(CustomHeaderConstants.X_CLIENT_CONTEXT, JsonUtil.toJsonString(principal));
                    } else if (principal instanceof LoginUser) {
                        h.set(CustomHeaderConstants.X_USER_CONTEXT, JsonUtil.toJsonString(principal));
                    }
                    if (CollectionUtils.isNotEmpty(authentication.getAuthorities())) {
                        Set<String> permissions = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
                        h.set(CustomHeaderConstants.X_PERMISSIONS, JsonUtil.toJsonString(permissions));
                    }
                })
                .build();
    }

    private boolean isPermitAllPath(String path) {
        return isPathPermitted(permitAllProperties.getIgnores(), path)
                || isPathPermitted(permitAllAnnotations, path);
    }

    private boolean isPathDenied(String path) {
        return permitAllProperties.getDenys() != null
                && permitAllProperties.getDenys().stream()
                .anyMatch(pattern -> PATH_MATCHER.match(pattern, path));
    }

}
