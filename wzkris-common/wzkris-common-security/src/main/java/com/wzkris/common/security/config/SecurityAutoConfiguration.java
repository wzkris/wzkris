package com.wzkris.common.security.config;

import com.wzkris.common.security.aspect.CheckPermsAspect;
import com.wzkris.common.security.handler.SecurityExceptionHandler;
import com.wzkris.common.security.utils.SecurityUtil;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Import;

@Import({ResourceServerConfig.class, SecurityUtil.class,
        CheckPermsAspect.class, SecurityExceptionHandler.class})
@AutoConfiguration
public class SecurityAutoConfiguration {

}
