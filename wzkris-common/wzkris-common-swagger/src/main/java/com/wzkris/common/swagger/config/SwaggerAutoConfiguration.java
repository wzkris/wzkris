package com.wzkris.common.swagger.config;

import com.wzkris.common.swagger.support.OpenApiMethodPredicates;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * Knife4j / SpringDoc 公共装配：双分组——默认分组仅收录带
 * {@code io.swagger.v3.oas.annotations.tags.Tag} 的业务接口，remote 分组收录服务间远程调用接口。
 */
@EnableConfigurationProperties(SwaggerProperties.class)
@ConditionalOnProperty(name = "springdoc.enabled", matchIfMissing = true)
@AutoConfiguration
public class SwaggerAutoConfiguration {

    private final SwaggerProperties swaggerProperties;

    public SwaggerAutoConfiguration(SwaggerProperties swaggerProperties) {
        this.swaggerProperties = swaggerProperties;
    }

    @Bean
    public GroupedOpenApi groupedOpenApi() {
        return GroupedOpenApi.builder()
                .group(swaggerProperties.getApiGroup())
                .addOpenApiMethodFilter(OpenApiMethodPredicates::isBusiness)
                .build();
    }

    @Bean
    public GroupedOpenApi remoteGroupedOpenApi() {
        return GroupedOpenApi.builder()
                .group(swaggerProperties.getRemoteGroup())
                .addOpenApiMethodFilter(OpenApiMethodPredicates::isRemoteController)
                .build();
    }

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().info(buildInfo());
    }

    private Info buildInfo() {
        return new Info()
                .title(swaggerProperties.getTitle())
                .description(swaggerProperties.getDescription())
                .license(new License().url(swaggerProperties.getLicense()))
                .version(swaggerProperties.getVersion())
                .termsOfService(swaggerProperties.getTermsOfServiceUrl());
    }

}
