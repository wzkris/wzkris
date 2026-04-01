package com.wzkris.common.remote.config;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.remote.annotation.EnableRemoteInterfaces;
import com.wzkris.common.remote.annotation.RemoteInterface;
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

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Registrar that turns {@link RemoteInterface} interfaces into Spring beans.
 */
public class RemoteInterfaceRegistrar implements ImportBeanDefinitionRegistrar,
        ResourceLoaderAware, EnvironmentAware {

    private ResourceLoader resourceLoader;

    private Environment environment;

    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
        ClassPathScanningCandidateComponentProvider scanner = buildScanner();

        Set<String> basePackages = getBasePackages(metadata);
        for (String basePackage : basePackages) {
            scanner.findCandidateComponents(basePackage).forEach(candidate -> {
                registerRemoteInterface(registry, candidate.getBeanClassName());
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
        scanner.addIncludeFilter(new AnnotationTypeFilter(RemoteInterface.class));
        return scanner;
    }

    private void registerRemoteInterface(BeanDefinitionRegistry registry, String className) {
        try {
            ClassLoader classLoader = resourceLoader != null
                    ? resourceLoader.getClassLoader()
                    : ClassUtils.getDefaultClassLoader();
            Class<?> beanClass = ClassUtils.forName(className, classLoader);
            AnnotationMetadata metadata = AnnotationMetadata.introspect(beanClass);
            MergedAnnotations annotations = metadata.getAnnotations();
            MergedAnnotation<RemoteInterface> attributes = annotations.get(RemoteInterface.class);

            String url = resolvePlaceholders(attributes.getString("url"));
            String serviceId = resolvePlaceholders(attributes.getString("serviceId"));
            String path = resolvePlaceholders(attributes.getString("path"));
            Class<?> fallbackFactory = attributes.getClass("fallbackFactory");

            // 验证：必须提供 url 或 serviceId 中的一个
            boolean hasUrl = StringUtils.hasText(url);
            boolean hasServiceId = StringUtils.hasText(serviceId);

            if (!hasUrl && !hasServiceId) {
                throw new IllegalStateException(
                        "@RemoteInterface must specify either 'url' or 'serviceId': " + className);
            }
            validateResultReturnType(beanClass);

            BeanDefinitionBuilder builder =
                    BeanDefinitionBuilder.genericBeanDefinition(RemoteInterfaceFactoryBean.class);
            builder.addPropertyValue("type", beanClass);
            builder.addPropertyValue("url", url);
            builder.addPropertyValue("serviceId", serviceId);
            builder.addPropertyValue("path", path);
            builder.addPropertyValue("fallbackFactory", fallbackFactory);
            builder.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
            builder.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);

            String beanName = determineBeanName(beanClass);
            registry.registerBeanDefinition(beanName, builder.getBeanDefinition());
        } catch (ClassNotFoundException | LinkageError ex) {
            throw new IllegalStateException("Failed to register RemoteInterface: " + className, ex);
        }
    }

    private String determineBeanName(Class<?> beanClass) {
        return StringUtils.uncapitalize(ClassUtils.getShortName(beanClass));
    }

    private void validateResultReturnType(Class<?> beanClass) {
        for (Method method : beanClass.getMethods()) {
            if (method.getDeclaringClass() == Object.class) {
                continue;
            }
            if (Modifier.isStatic(method.getModifiers()) || method.isDefault()) {
                continue;
            }
            if (!Result.class.equals(method.getReturnType())) {
                throw new IllegalStateException(
                        "@RemoteInterface interface method return type must be Result: "
                                + beanClass.getName() + "#" + method.getName());
            }
        }
    }

    private Set<String> getBasePackages(AnnotationMetadata metadata) {
        Set<String> basePackages = new HashSet<>();
        Map<String, Object> attributes = metadata
                .getAnnotationAttributes(EnableRemoteInterfaces.class.getCanonicalName());

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
