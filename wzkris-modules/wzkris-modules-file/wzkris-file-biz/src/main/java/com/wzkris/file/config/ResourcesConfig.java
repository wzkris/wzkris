package com.wzkris.file.config;

import com.wzkris.common.oss.config.OssConfig;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 本地文件映射配置
 *
 * @author wzkris
 */
@RefreshScope
@Configuration
public class ResourcesConfig implements WebMvcConfigurer {

    private final OssConfig.LocalProperties localProperties;

    public ResourcesConfig(OssConfig ossConfig) {
        localProperties = ossConfig.getLocal();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 规范化本地路径
        Path resolvedPath = Paths.get(localProperties.getPath()).normalize();
        // 映射访问路径
        registry.addResourceHandler(localProperties.getPrefix() + "/**")
                .addResourceLocations("file:" + resolvedPath.toAbsolutePath() + "/");
    }

    /**
     * 开启跨域
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 设置允许跨域的路由
        registry.addMapping(localProperties.getPrefix() + "/**")
                // 设置允许跨域请求的域名
                .allowedOrigins("*")
                // 设置允许的方法
                .allowedMethods("*");
    }
}
