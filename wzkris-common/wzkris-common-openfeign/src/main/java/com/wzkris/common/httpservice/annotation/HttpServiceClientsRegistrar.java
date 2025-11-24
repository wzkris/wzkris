package com.wzkris.common.httpservice.annotation;

import com.wzkris.common.httpservice.factory.HttpServiceClientFactoryBean;
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
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.HashSet;
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
        Set<String> basePackages = getBasePackages(metadata);
        if (basePackages.isEmpty()) {
            basePackages.add(ClassUtils.getPackageName(metadata.getClassName()));
        }

        ClassPathScanningCandidateComponentProvider scanner = buildScanner();
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
            if (attributes == null) {
                return;
            }

            String serviceId = resolvePlaceholders(attributes.getString("serviceId"));
            String contextId = resolvePlaceholders(attributes.getString("contextId"));
            Class<?> fallbackFactory = attributes.getClass("fallbackFactory");
            Assert.hasText(serviceId, "@HttpServiceClient serviceId must not be empty: " + className);

            BeanDefinitionBuilder builder =
                    BeanDefinitionBuilder.genericBeanDefinition(HttpServiceClientFactoryBean.class);
            builder.addPropertyValue("type", beanClass);
            builder.addPropertyValue("serviceId", serviceId);
            builder.addPropertyValue("fallbackFactoryClass", fallbackFactory);
            builder.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
            builder.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);

            String beanName = determineBeanName(beanClass, contextId);
            registry.registerBeanDefinition(beanName, builder.getBeanDefinition());
        } catch (ClassNotFoundException | LinkageError ex) {
            throw new IllegalStateException("Failed to register HttpServiceClient: " + className, ex);
        }
    }

    private String determineBeanName(Class<?> beanClass, String contextId) {
        if (StringUtils.hasText(contextId)) {
            return contextId;
        }
        return StringUtils.uncapitalize(ClassUtils.getShortName(beanClass));
    }

    private Set<String> getBasePackages(AnnotationMetadata metadata) {
        Set<String> packages = new HashSet<>();
        MergedAnnotation<EnableHttpServiceClients> attributes =
                metadata.getAnnotations().get(EnableHttpServiceClients.class);
        if (attributes == null) {
            return packages;
        }
        Arrays.stream(attributes.getStringArray("basePackages"))
                .map(this::normalizePackage)
                .filter(StringUtils::hasText)
                .forEach(packages::add);
        Arrays.stream(attributes.getClassArray("basePackageClasses"))
                .map(ClassUtils::getPackageName)
                .filter(StringUtils::hasText)
                .forEach(packages::add);
        return packages;
    }

    private String normalizePackage(String pkg) {
        if (!StringUtils.hasText(pkg)) {
            return pkg;
        }
        int starIndex = pkg.indexOf('*');
        if (starIndex > 0) {
            pkg = pkg.substring(0, starIndex);
            while (pkg.endsWith(".")) {
                pkg = pkg.substring(0, pkg.length() - 1);
            }
            return pkg;
        }
        return pkg;
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

