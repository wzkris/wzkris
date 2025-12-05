package com.wzkris.auth.security.core;

import com.wzkris.auth.constants.OAuth2ParameterConstant;
import com.wzkris.auth.enums.LoginTypeEnum;
import com.wzkris.common.core.enums.AuthTypeEnum;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author wzkris
 * @date 2024/3/11
 * @description 模式转换器基类，将参数转换为Authentication
 */
public abstract class CommonAuthenticationConverter<T extends CommonAuthenticationToken>
        implements AuthenticationConverter {

    private final String LOGIN_TYPE = "login_type";

    /**
     * 是否支持此convert
     *
     * @param loginType 授权类型
     * @return 是否支持
     */
    protected abstract boolean support(LoginTypeEnum loginType);

    /**
     * 校验参数
     *
     * @param parameters 请求参数
     */
    protected abstract void checkParams(MultiValueMap<String, String> parameters);

    @Override
    public final CommonAuthenticationToken convert(HttpServletRequest request) {
        // login_type (REQUIRED)
        LoginTypeEnum loginTypeEnum = LoginTypeEnum.fromValue(request.getParameter(LOGIN_TYPE));
        if (loginTypeEnum == null || !this.support(loginTypeEnum)) {
            return null;
        }

        // auth_type (REQUIRED)
        AuthTypeEnum authTypeEnum = AuthTypeEnum.fromValue(request.getParameter(OAuth2ParameterConstant.AUTH_TYPE));
        if (authTypeEnum == null) {
            return null;
        }

        MultiValueMap<String, String> parameters = getParameters(request);

        this.checkParams(parameters);

        // 扩展信息
        Map<String, Object> additionalParameters = parameters.entrySet().stream()
                .filter(e -> !e.getKey().equals(LOGIN_TYPE) && !e.getKey().equals(OAuth2ParameterConstant.AUTH_TYPE))
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().get(0)));

        // 创建待认证token
        return this.buildToken(authTypeEnum, additionalParameters);
    }

    /**
     * 构建AuthenticationToken
     */
    protected abstract CommonAuthenticationToken buildToken(AuthTypeEnum authTypeEnum, Map<String, Object> additionalParameters);

    final MultiValueMap<String, String> getParameters(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>(parameterMap.size());
        parameterMap.forEach((key, values) -> {
            for (String value : values) {
                parameters.add(key, value);
            }
        });
        return parameters;
    }

}
