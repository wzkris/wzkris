package com.wzkris.common.orm.config;

import com.wzkris.common.orm.annotation.aspect.IgnoreTenantAspect;
import com.wzkris.common.orm.filter.DynamicTenantSwitchingFilter;
import com.wzkris.common.orm.handler.MybatisExceptionHandler;
import com.wzkris.common.orm.plus.config.MybatisPlusConfig;
import com.wzkris.common.orm.plus.config.TenantProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Import;

@Import({TenantProperties.class, MybatisPlusConfig.class, DataScopeConfig.class,
        DynamicTenantSwitchingFilter.class, IgnoreTenantAspect.class, MybatisExceptionHandler.class})
@AutoConfiguration
public class OrmAutoConfiguration {

}
