package com.wzkris.common.security.config;

import com.wzkris.common.core.support.UserContextHelper;
import com.wzkris.common.security.aspect.CheckPermsAspect;
import com.wzkris.common.security.handler.SecurityExceptionHandler;
import com.wzkris.common.security.support.SecurityUserContextHelper;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@Import({ResourceServerConfiguration.class})
@AutoConfiguration
public class SecurityAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public UserContextHelper userContextHelper() {
        return new SecurityUserContextHelper();
    }

    @Bean
    public CheckPermsAspect checkPermsAspect() {
        return new CheckPermsAspect();
    }

    @Bean
    public SecurityExceptionHandler securityExceptionHandler() {
        return new SecurityExceptionHandler();
    }

}
