package com.wzkris.common.web.config;

import com.wzkris.common.orm.utils.DynamicTenantUtil;
import com.wzkris.common.web.handler.GlobalDynamicTenantInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@ConditionalOnClass(value = DynamicTenantUtil.class) // 动态租户拦截依赖orm模块
public class TenantInterceptorConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 全局动态租户拦截器
        registry.addInterceptor(new GlobalDynamicTenantInterceptor());
    }

}
