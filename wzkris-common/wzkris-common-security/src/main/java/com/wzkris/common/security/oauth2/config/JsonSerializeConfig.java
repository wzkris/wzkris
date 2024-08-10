package com.wzkris.common.security.oauth2.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wzkris.common.security.oauth2.deserializer.module.OAuth2JacksonModule;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : json序列化OAuth2增强
 * @date : 2024/08/05 11:28
 */
public class JsonSerializeConfig implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof ObjectMapper objectMapper) {
            objectMapper.registerModules(new OAuth2JacksonModule());
        }
        return bean;
    }
}