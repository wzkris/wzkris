package com.wzkris.auth.oauth2.utils;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.springframework.security.jackson2.SecurityJackson2Modules;
import org.springframework.security.oauth2.server.authorization.jackson2.OAuth2AuthorizationServerJackson2Module;

import java.util.List;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : OAuth2 json序列化工具
 * @date : 2024/08/19 17:06
 */
public final class OAuth2JsonUtil {

    @Getter
    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        ClassLoader classLoader = OAuth2JsonUtil.class.getClassLoader();
        List<Module> securityModules = SecurityJackson2Modules.getModules(classLoader);
        objectMapper.registerModules(securityModules);
        objectMapper.registerModules(new OAuth2AuthorizationServerJackson2Module());
    }

}
