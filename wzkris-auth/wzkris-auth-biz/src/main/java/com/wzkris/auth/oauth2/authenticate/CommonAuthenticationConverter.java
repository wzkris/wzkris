package com.wzkris.auth.oauth2.authenticate;

import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.security.oauth2.utils.OAuth2EndpointUtil;
import com.wzkris.common.security.oauth2.utils.OAuth2ExceptionUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationGrantAuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author wzkris
 * @date 2024/3/11
 * @description 模式转换器基类，将参数转换为Authentication
 */
public abstract class CommonAuthenticationConverter<T extends OAuth2AuthorizationGrantAuthenticationToken> implements AuthenticationConverter {

    /**
     * 是否支持此convert
     *
     * @param grantType 授权类型
     * @return 是否支持
     */
    protected abstract boolean support(String grantType);

    /**
     * 校验参数
     *
     * @param parameters 请求参数
     */
    public abstract void checkParams(MultiValueMap<String, String> parameters);

    @Override
    public final Authentication convert(HttpServletRequest request) {
        // grant_type (REQUIRED)
        String grantType = request.getParameter(OAuth2ParameterNames.GRANT_TYPE);
        if (!this.support(grantType)) {
            return null;
        }

        MultiValueMap<String, String> parameters = OAuth2EndpointUtil.getParameters(request);
        // scope (OPTIONAL)
        String scope = parameters.getFirst(OAuth2ParameterNames.SCOPE);
        if (StringUtils.hasText(scope) && parameters.get(OAuth2ParameterNames.SCOPE).size() != 1) {
            OAuth2ExceptionUtil.throwErrorI18n(OAuth2ErrorCodes.INVALID_SCOPE, "oauth2.scope.invalid");
        }

        Set<String> requestedScopes = null;
        if (StringUtils.hasText(scope)) {
            requestedScopes = new HashSet<>(Arrays.asList(StringUtils.delimitedListToStringArray(scope, StringUtil.COMMA)));
        }

        // 校验个性化参数
        this.checkParams(parameters);

        // 获取当前已经认证的客户端信息
        Authentication clientPrincipal = SecurityContextHolder.getContext().getAuthentication();
        if (clientPrincipal == null) {
            OAuth2ExceptionUtil.throwErrorI18n(OAuth2ErrorCodes.INVALID_CLIENT, "oauth2.client.invalid");
        }

        // 扩展信息
        Map<String, Object> additionalParameters = parameters.entrySet()
                .stream()
                .filter(e -> !e.getKey().equals(OAuth2ParameterNames.GRANT_TYPE) && !e.getKey().equals(OAuth2ParameterNames.SCOPE))
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().get(0)));

        // 创建待认证token
        return this.buildToken(clientPrincipal, requestedScopes, additionalParameters);
    }

    /**
     * 构建AuthenticationToken
     */
    protected abstract CommonAuthenticationToken buildToken(Authentication clientPrincipal, Set<String> requestedScopes, Map<String, Object> additionalParameters);
}
