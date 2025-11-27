package com.wzkris.common.orm.config;

import com.wzkris.common.orm.aspect.DataScopeAspect;
import com.wzkris.common.orm.handler.MybatisExceptionHandler;
import com.wzkris.common.orm.plus.config.MybatisPlusConfig;
import com.wzkris.common.orm.plus.config.TenantProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

@Import({MybatisPlusConfig.class, DataScopeConfig.class,
        MybatisExceptionHandler.class, DataScopeAspect.class})
@EnableConfigurationProperties({TenantProperties.class})
@AutoConfiguration
public class OrmAutoConfiguration {

}
