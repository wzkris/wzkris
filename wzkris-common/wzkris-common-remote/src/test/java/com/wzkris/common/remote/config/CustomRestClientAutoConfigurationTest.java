package com.wzkris.common.remote.config;

import com.wzkris.common.remote.properties.RemoteInterfaceProperties;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.JdkClientHttpRequestFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.http.HttpClient;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("CustomRestClientAutoConfiguration 单测")
class CustomRestClientAutoConfigurationTest {

    @Test
    @DisplayName("默认情况下httpClient和requestFactory共用自定义executor")
    void testBuildFactoryUsesSameExecutorByDefault() throws Exception {
        RemoteInterfaceProperties properties = new RemoteInterfaceProperties();

        JdkClientHttpRequestFactory factory = unwrapJdkFactory(invokeBuildFactory(properties));
        HttpClient httpClient = readField(factory, "httpClient", HttpClient.class);
        Executor requestFactoryExecutor = readField(factory, "executor", Executor.class);

        assertTrue(httpClient.executor().isPresent());
        assertSame(httpClient.executor().orElseThrow(), requestFactoryExecutor);
    }

    @Test
    @DisplayName("自定义executor应固定使用虚拟线程")
    void testBuildFactoryUsesVirtualThreads() throws Exception {
        RemoteInterfaceProperties properties = new RemoteInterfaceProperties();
        properties.getHttp().setExecutorThreadNamePrefix("remote-vt-");

        JdkClientHttpRequestFactory factory = unwrapJdkFactory(invokeBuildFactory(properties));
        Executor requestFactoryExecutor = readField(factory, "executor", Executor.class);

        CompletableFuture<Thread> future = CompletableFuture.supplyAsync(Thread::currentThread, requestFactoryExecutor);
        Thread actualThread = future.get(5, TimeUnit.SECONDS);

        assertTrue(actualThread.isVirtual());
        assertTrue(actualThread.getName().startsWith("remote-vt-"));
    }

    private BufferingClientHttpRequestFactory invokeBuildFactory(RemoteInterfaceProperties properties) throws Exception {
        Method method = CustomRestClientAutoConfiguration.class.getDeclaredMethod("buildFactory", RemoteInterfaceProperties.class);
        method.setAccessible(true);
        return (BufferingClientHttpRequestFactory) method.invoke(null, properties);
    }

    private JdkClientHttpRequestFactory unwrapJdkFactory(BufferingClientHttpRequestFactory requestFactory) {
        return (JdkClientHttpRequestFactory) requestFactory.getDelegate();
    }

    private <T> T readField(Object target, String fieldName, Class<T> fieldType) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return fieldType.cast(field.get(target));
    }

}
