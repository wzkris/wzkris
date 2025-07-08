package com.wzkris.common.openfeign;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.jackson2.SecurityJackson2Modules;

import java.util.List;

@Slf4j
@DisplayName("UsernamePasswordAuthenticationToken序列化测试")
public class UsernamePasswordAuthenticationTokenTest {

    ObjectMapper objectMapper = new ObjectMapper();

    public UsernamePasswordAuthenticationTokenTest() {
        ClassLoader classLoader = this.getClass().getClassLoader();
        List<Module> securityModules = SecurityJackson2Modules.getModules(classLoader);
        objectMapper.registerModules(securityModules);
    }

    @Test
    public void serialize() throws JsonProcessingException {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken("username", "password");
        String str = objectMapper.writeValueAsString(authenticationToken);
        log.info(str);
        UsernamePasswordAuthenticationToken readValue = objectMapper.readValue(str, UsernamePasswordAuthenticationToken.class);
        log.info("{}", readValue);
    }

}
