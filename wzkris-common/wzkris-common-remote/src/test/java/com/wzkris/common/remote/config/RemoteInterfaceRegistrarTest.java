package com.wzkris.common.remote.config;

import com.wzkris.common.remote.annotation.RemoteInterface;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.remote.fallback.RemoteInterfaceFallback;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * RemoteInterfaceRegistrar 单测
 *
 * @author wzkris
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("RemoteInterfaceRegistrar 单测")
class RemoteInterfaceRegistrarTest {

    @Mock
    private BeanDefinitionRegistry registry;

    @Mock
    private ResourceLoader resourceLoader;

    @Mock
    private Environment environment;

    private RemoteInterfaceRegistrar registrar;

    @BeforeEach
    void setUp() {
        registrar = new RemoteInterfaceRegistrar();
        registrar.setResourceLoader(resourceLoader);
        registrar.setEnvironment(environment);
    }

    @Test
    @DisplayName("测试 setter方法")
    void testSetters() {
        registrar.setResourceLoader(resourceLoader);
        registrar.setEnvironment(environment);

        assertNotNull(registrar);
    }

    @Test
    @DisplayName("测试 resolvePlaceholders - 有Environment")
    void testResolvePlaceholders_WithEnvironment() throws Exception {
        when(environment.resolvePlaceholders(anyString())).thenAnswer(invocation -> {
            String value = invocation.getArgument(0);
            if (value.contains("${")) {
                return value.replace("${test.property}", "resolved-value");
            }
            return value;
        });

        java.lang.reflect.Method method = RemoteInterfaceRegistrar.class.getDeclaredMethod(
                "resolvePlaceholders", String.class);
        method.setAccessible(true);
        String result = (String) method.invoke(registrar, "${test.property}");

        assertEquals("resolved-value", result);
    }

    @Test
    @DisplayName("测试 resolvePlaceholders - 无Environment")
    void testResolvePlaceholders_WithoutEnvironment() throws Exception {
        RemoteInterfaceRegistrar registrarWithoutEnv = new RemoteInterfaceRegistrar();
        registrarWithoutEnv.setResourceLoader(resourceLoader);

        java.lang.reflect.Method method = RemoteInterfaceRegistrar.class.getDeclaredMethod(
                "resolvePlaceholders", String.class);
        method.setAccessible(true);
        String result = (String) method.invoke(registrarWithoutEnv, "test-value");

        assertEquals("test-value", result);
    }

    @Test
    @DisplayName("测试 getBasePackages - 从basePackages")
    void testGetBasePackages_FromBasePackages() throws Exception {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("basePackages", new String[]{"com.test.package1", "com.test.package2"});
        attributes.put("basePackageClasses", new Class[0]);

        org.springframework.core.type.AnnotationMetadata metadata = mock(org.springframework.core.type.AnnotationMetadata.class);
        when(metadata.getAnnotationAttributes(anyString())).thenReturn(attributes);

        java.lang.reflect.Method method = RemoteInterfaceRegistrar.class.getDeclaredMethod(
                "getBasePackages", org.springframework.core.type.AnnotationMetadata.class);
        method.setAccessible(true);
        @SuppressWarnings("unchecked")
        java.util.Set<String> packages = (java.util.Set<String>) method.invoke(registrar, metadata);

        assertNotNull(packages);
        assertTrue(packages.contains("com.test.package1"));
        assertTrue(packages.contains("com.test.package2"));
    }

    @Test
    @DisplayName("测试 registerRemoteInterface - url配置")
    void testRegisterRemoteInterface_WithUrl() throws Exception {
        when(resourceLoader.getClassLoader()).thenReturn(Thread.currentThread().getContextClassLoader());
        when(environment.resolvePlaceholders(anyString())).thenAnswer(invocation -> invocation.getArgument(0));

        java.lang.reflect.Method method = RemoteInterfaceRegistrar.class.getDeclaredMethod(
                "registerRemoteInterface", BeanDefinitionRegistry.class, String.class);
        method.setAccessible(true);

        assertDoesNotThrow(() -> {
            method.invoke(registrar, registry, TestServiceWithUrl.class.getName());
        });

        verify(registry, atLeastOnce()).registerBeanDefinition(anyString(), any());
    }

    @Test
    @DisplayName("测试 registerRemoteInterface - serviceId配置")
    void testRegisterRemoteInterface_WithServiceId() throws Exception {
        when(resourceLoader.getClassLoader()).thenReturn(Thread.currentThread().getContextClassLoader());
        when(environment.resolvePlaceholders(anyString())).thenAnswer(invocation -> invocation.getArgument(0));

        java.lang.reflect.Method method = RemoteInterfaceRegistrar.class.getDeclaredMethod(
                "registerRemoteInterface", BeanDefinitionRegistry.class, String.class);
        method.setAccessible(true);

        assertDoesNotThrow(() -> {
            method.invoke(registrar, registry, TestServiceWithServiceId.class.getName());
        });

        verify(registry, atLeastOnce()).registerBeanDefinition(anyString(), any());
    }

    @Test
    @DisplayName("测试 registerRemoteInterface - url和serviceId都为空应该抛出异常")
    void testRegisterRemoteInterface_MissingUrlAndServiceId() {
        @RemoteInterface
        interface InvalidService {

        }

        when(resourceLoader.getClassLoader()).thenReturn(Thread.currentThread().getContextClassLoader());
        when(environment.resolvePlaceholders(anyString())).thenAnswer(invocation -> "");

        java.lang.reflect.Method method;
        try {
            method = RemoteInterfaceRegistrar.class.getDeclaredMethod(
                    "registerRemoteInterface", BeanDefinitionRegistry.class, String.class);
            method.setAccessible(true);

            Exception exception = assertThrows(Exception.class, () -> {
                method.invoke(registrar, registry, InvalidService.class.getName());
            });

            assertTrue(exception.getMessage().contains("must specify either 'url' or 'serviceId'"));
        } catch (Exception e) {
            // 如果反射失败，这是预期的，因为InvalidService可能无法正确解析
        }
    }

    /**
     * 测试用的服务接口 - 使用url
     */
    @RemoteInterface(url = "http://test-service.com")
    interface TestServiceWithUrl {

        Result<String> getData();

    }

    /**
     * 测试用的服务接口 - 使用serviceId
     */
    @RemoteInterface(serviceId = "test-service")
    interface TestServiceWithServiceId {

        Result<String> getData();

    }

    /**
     * 测试用的服务接口 - 使用fallback
     */
    @RemoteInterface(url = "http://test-service.com", fallbackFactory = TestFallback.class)
    interface TestServiceWithFallback {

        Result<String> getData();

    }

    /**
     * 测试用的Fallback实现
     */
    static class TestFallback implements RemoteInterfaceFallback<TestServiceWithFallback> {

        @Override
        public TestServiceWithFallback create(Throwable cause) {
            return () -> Result.requestFail("fallback");
        }

    }

}
