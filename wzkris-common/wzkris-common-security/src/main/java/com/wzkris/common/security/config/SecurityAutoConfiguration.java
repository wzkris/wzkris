package com.wzkris.common.security.config;

import com.wzkris.common.security.oauth2.ResourceServerConfig;
import com.wzkris.common.security.utils.LoginCustomerUtil;
import com.wzkris.common.security.utils.LoginUserUtil;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Import;

@Import({PermitAllProperties.class, ResourceServerConfig.class,
        LoginUserUtil.class, LoginCustomerUtil.class})
@AutoConfiguration
public class SecurityAutoConfiguration {

}
