package com.thingslink.common.security.utils;

import com.thingslink.common.security.exception.OAuth2AuthenticationI18nException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Map;

public class OAuth2EndpointUtil {

    private static final String ACCESS_TOKEN_REQUEST_ERROR_URI = "https://datatracker.ietf.org/doc/html/rfc6749#section-5.2";

    public static MultiValueMap<String, String> getParameters(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>(parameterMap.size());
        parameterMap.forEach((key, values) -> {
            if (values.length > 0) {
                for (String value : values) {
                    parameters.add(key, value);
                }
            }
        });
        return parameters;
    }

    public static void throwError(String errorCode, String description) {
        throw new OAuth2AuthenticationException(
                new OAuth2Error(errorCode, description, ACCESS_TOKEN_REQUEST_ERROR_URI)
        );
    }

    public static void throwErrorI18n(String errorCode, String code, Object... args) {
        throw new OAuth2AuthenticationI18nException(errorCode, code, args);
    }
}
