package com.wzkris.common.security.oauth2.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.wzkris.common.core.utils.json.JsonUtil;
import com.wzkris.common.security.oauth2.deserializer.GrantedAuthorityDeserializer;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.core.GrantedAuthority;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : json序列化OAuth2增强
 * @date : 2024/08/05 11:28
 */
public class JsonSerializeConfig implements InitializingBean {

    @Override
    public void afterPropertiesSet() throws Exception {
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addDeserializer(GrantedAuthority.class, new GrantedAuthorityDeserializer());
        ObjectMapper objectMapper = JsonUtil.getObjectMapper();
        objectMapper.registerModules(simpleModule);
    }
}
