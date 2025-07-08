package com.wzkris.common.openfeign;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.jackson2.SecurityJackson2Modules;

import java.util.List;

@Slf4j
@DisplayName("SecurityContext序列化测试")
public class SecurityContextTest {

    ObjectMapper objectMapper = new ObjectMapper();

    public SecurityContextTest() {
        ClassLoader classLoader = this.getClass().getClassLoader();
        List<Module> securityModules = SecurityJackson2Modules.getModules(classLoader);
        objectMapper.registerModules(securityModules);
    }

    @Test
    public void authenticationTokenSerialize() throws JsonProcessingException {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken("username", "password");
        String str = objectMapper.writeValueAsString(authenticationToken);
        log.info(str);
        UsernamePasswordAuthenticationToken readValue = objectMapper.readValue(str, UsernamePasswordAuthenticationToken.class);
        log.info("{}", readValue);
    }

    @Test
    public void securityContextSerialize() throws JsonProcessingException {
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken("username", "password");
        securityContext.setAuthentication(authenticationToken);
        String str = objectMapper.writeValueAsString(securityContext);
        log.info(str);
        SecurityContext context = objectMapper.readValue(str, SecurityContext.class);
        log.info("{}", context);
    }

}
