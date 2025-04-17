package com.wzkris.file.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

/**
 * 通用映射配置
 *
 * @author wzkris
 */
@Configuration
public class ResourcesConfig implements WebMvcConfigurer {

    @Autowired
    private LocalProperties localProperties;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 映射访问路径
        registry.addResourceHandler(localProperties.getPrefix() + File.separator + "**")
                .addResourceLocations("file:" + localProperties.getPath() + File.separator);
    }

    /**
     * 开启跨域
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 设置允许跨域的路由
        registry.addMapping(localProperties.getPrefix() + File.separator + "**")
                // 设置允许跨域请求的域名
                .allowedOrigins("*")
                // 设置允许的方法
                .allowedMethods("*");
    }
}
