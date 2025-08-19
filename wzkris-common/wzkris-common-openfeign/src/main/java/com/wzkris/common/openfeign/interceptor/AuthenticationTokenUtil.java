package com.wzkris.common.openfeign.interceptor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wzkris.common.core.exception.util.UtilException;
import org.springframework.security.jackson2.SecurityJackson2Modules;

import java.util.List;

class AuthenticationTokenUtil {

    static ObjectMapper objectMapper = new ObjectMapper();

    public AuthenticationTokenUtil() {
        ClassLoader classLoader = this.getClass().getClassLoader();
        List<Module> securityModules = SecurityJackson2Modules.getModules(classLoader);
        objectMapper.registerModules(securityModules);
    }

    public static String serialize(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new UtilException(e.getMessage());
        }
    }

    public static <T> T deserialize(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            throw new UtilException(e.getMessage());
        }
    }

}
