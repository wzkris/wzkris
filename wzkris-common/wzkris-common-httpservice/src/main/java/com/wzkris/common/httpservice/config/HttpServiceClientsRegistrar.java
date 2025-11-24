package com.wzkris.common.httpservice.config;

import com.wzkris.common.httpservice.annotation.EnableHttpServiceClients;
import com.wzkris.common.httpservice.annotation.HttpServiceClient;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Registrar that turns {@link HttpServiceClient} interfaces into Spring beans.
 */
public class HttpServiceClientsRegistrar implements ImportBeanDefinitionRegistrar,
        ResourceLoaderAware, EnvironmentAware {

    private ResourceLoader resourceLoader;

    private Environment environment;

    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
        ClassPathScanningCandidateComponentProvider scanner = buildScanner();

        Set<String> basePackages = getBasePackages(metadata);
        for (String basePackage : basePackages) {
            scanner.findCandidateComponents(basePackage).forEach(candidate -> {
                registerHttpServiceClient(registry, candidate.getBeanClassName());
            });
        }
    }

    private ClassPathScanningCandidateComponentProvider buildScanner() {
        ClassPathScanningCandidateComponentProvider scanner =
                new ClassPathScanningCandidateComponentProvider(false, environment) {
                    @Override
                    protected boolean isCandidateComponent(
                            org.springframework.beans.factory.annotation.AnnotatedBeanDefinition beanDefinition) {
                        return beanDefinition.getMetadata().isInterface();
                    }
                };
        scanner.setResourceLoader(resourceLoader);
        scanner.addIncludeFilter(new AnnotationTypeFilter(HttpServiceClient.class));
        return scanner;
    }

    private void registerHttpServiceClient(BeanDefinitionRegistry registry, String className) {
        try {
            ClassLoader classLoader = resourceLoader != null
                    ? resourceLoader.getClassLoader()
                    : ClassUtils.getDefaultClassLoader();
            Class<?> beanClass = ClassUtils.forName(className, classLoader);
            AnnotationMetadata metadata = AnnotationMetadata.introspect(beanClass);
            MergedAnnotations annotations = metadata.getAnnotations();
            MergedAnnotation<HttpServiceClient> attributes = annotations.get(HttpServiceClient.class);

            String url = resolvePlaceholders(attributes.getString("url"));
            String serviceId = resolvePlaceholders(attributes.getString("serviceId"));
            Class<?> fallbackFactory = attributes.getClass("fallbackFactory");

            // 验证：必须提供 url 或 serviceId 中的一个
            boolean hasUrl = StringUtils.hasText(url);
            boolean hasServiceId = StringUtils.hasText(serviceId);

            if (!hasUrl && !hasServiceId) {
                throw new IllegalStateException(
                        "@HttpServiceClient must specify either 'url' or 'serviceId': " + className);
            }

            BeanDefinitionBuilder builder =
                    BeanDefinitionBuilder.genericBeanDefinition(HttpServiceClientFactoryBean.class);
            builder.addPropertyValue("type", beanClass);
            builder.addPropertyValue("url", url);
            builder.addPropertyValue("serviceId", serviceId);
            builder.addPropertyValue("fallbackFactory", fallbackFactory);
            builder.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
            builder.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);

            String beanName = determineBeanName(beanClass);
            registry.registerBeanDefinition(beanName, builder.getBeanDefinition());
        } catch (ClassNotFoundException | LinkageError ex) {
            throw new IllegalStateException("Failed to register HttpServiceClient: " + className, ex);
        }
    }

    private String determineBeanName(Class<?> beanClass) {
        return StringUtils.uncapitalize(ClassUtils.getShortName(beanClass));
    }

    private Set<String> getBasePackages(AnnotationMetadata metadata) {
        Set<String> basePackages = new HashSet<>();
        Map<String, Object> attributes = metadata
                .getAnnotationAttributes(EnableHttpServiceClients.class.getCanonicalName());

        for (String pkg : (String[]) attributes.get("basePackages")) {
            if (StringUtils.hasText(pkg)) {
                basePackages.add(pkg);
            }
        }
        for (Class<?> clazz : (Class[]) attributes.get("basePackageClasses")) {
            basePackages.add(ClassUtils.getPackageName(clazz));
        }
        return basePackages;
    }

    private String resolvePlaceholders(String value) {
        return environment != null ? environment.resolvePlaceholders(value) : value;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

}

