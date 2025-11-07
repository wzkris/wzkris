package com.wzkris.common.security.config;

import com.wzkris.common.security.annotation.aspect.CheckPermsAspect;
import com.wzkris.common.security.handler.SecurityExceptionHandler;
import com.wzkris.common.security.oauth2.ResourceServerConfig;
import com.wzkris.common.security.oauth2.filter.RequestSignatureFilter;
import com.wzkris.common.security.utils.AdminUtil;
import com.wzkris.common.security.utils.ClientUtil;
import com.wzkris.common.security.utils.CustomerUtil;
import com.wzkris.common.security.utils.StaffUtil;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Import;

@Import({ResourceServerConfig.class, RequestSignatureFilter.class,
        AdminUtil.class, StaffUtil.class, CustomerUtil.class, ClientUtil.class,
        CheckPermsAspect.class, SecurityExceptionHandler.class})
@AutoConfiguration
public class SecurityAutoConfiguration {

}
