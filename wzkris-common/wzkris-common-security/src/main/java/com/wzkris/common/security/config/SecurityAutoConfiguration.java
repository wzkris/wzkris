package com.wzkris.common.security.config;

import com.wzkris.common.security.annotation.aspect.CheckPermsAspect;
import com.wzkris.common.security.annotation.aspect.FieldPermsAspect;
import com.wzkris.common.security.handler.SecurityExceptionHandler;
import com.wzkris.common.security.oauth2.ResourceServerConfig;
import com.wzkris.common.security.oauth2.filter.RequestSignatureFilter;
import com.wzkris.common.security.utils.LoginClientUtil;
import com.wzkris.common.security.utils.LoginCustomerUtil;
import com.wzkris.common.security.utils.LoginStaffUtil;
import com.wzkris.common.security.utils.LoginUserUtil;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Import;

@Import({ResourceServerConfig.class, RequestSignatureFilter.class,
        LoginUserUtil.class, LoginStaffUtil.class, LoginCustomerUtil.class, LoginClientUtil.class,
        CheckPermsAspect.class, FieldPermsAspect.class, SecurityExceptionHandler.class})
@AutoConfiguration
public class SecurityAutoConfiguration {

}
