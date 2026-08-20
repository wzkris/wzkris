package com.wzkris.common.remote.config;

import com.wzkris.common.remote.fallback.RemoteInterfaceFallback;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.loadbalancer.DeferringLoadBalancerInterceptor;
import org.springframework.context.ApplicationContext;
import org.springframework.web.client.RestClient;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

/**
 * RemoteInterfaceFactoryBean 单测
 *
 * @author wzkris
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("RemoteInterfaceFactoryBean 单测")
class RemoteInterfaceFactoryBeanTest {

    @Mock
    private ApplicationContext applicationContext;

    @Mock
    private BeanFactory beanFactory;

    @Mock
    private RestClient.Builder sourceRestClientBuilder;

    @Mock
    private RestClient restClient;

    @Mock
    private RestClient.Builder clonedRestClientBuilder;

    @Mock
    private ObjectProvider<DeferringLoadBalancerInterceptor> loadBalancerInterceptorProvider;

    @Mock
    private DeferringLoadBalancerInterceptor loadBalancerInterceptor;

    private RemoteInterfaceFactoryBean<TestService> factoryBean;

    @BeforeEach
    void setUp() {
        factoryBean = new RemoteInterfaceFactoryBean<>();
        factoryBean.setType(TestService.class);
        factoryBean.setApplicationContext(applicationContext);
        factoryBean.setBeanFactory(beanFactory);
    }

    @Test
    @DisplayName("测试 afterPropertiesSet - 缺少type应该抛出异常")
    void testAfterPropertiesSet_MissingType() {
        RemoteInterfaceFactoryBean<TestService> bean = new RemoteInterfaceFactoryBean<>();
        bean.setUrl("http://test.com");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, bean::afterPropertiesSet);
        assertTrue(exception.getMessage().contains("必须提供 RemoteInterface 接口类型"));
    }

    @Test
    @DisplayName("测试 afterPropertiesSet - url和serviceId都为空应该抛出异常")
    void testAfterPropertiesSet_MissingUrlAndServiceId() {
        factoryBean.setType(TestService.class);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, factoryBean::afterPropertiesSet);
        assertTrue(exception.getMessage().contains("必须指定 'url' 或 'serviceId'"));
    }

    @Test
    @DisplayName("测试 afterPropertiesSet - url不以http开头应该抛出异常")
    void testAfterPropertiesSet_InvalidUrl() {
        factoryBean.setUrl("invalid-url");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, factoryBean::afterPropertiesSet);
        assertTrue(exception.getMessage().contains("url必须为http开头"));
    }

    @Test
    @DisplayName("测试 afterPropertiesSet - 使用url成功")
    void testAfterPropertiesSet_WithUrl() {
        factoryBean.setUrl("http://test.com");

        assertDoesNotThrow(factoryBean::afterPropertiesSet);
    }

    @Test
    @DisplayName("测试 afterPropertiesSet - 使用serviceId成功")
    void testAfterPropertiesSet_WithServiceId() {
        factoryBean.setServiceId("test-service");

        assertDoesNotThrow(factoryBean::afterPropertiesSet);
    }

    @Test
    @DisplayName("测试 getObjectType")
    void testGetObjectType() {
        factoryBean.setType(TestService.class);
        assertEquals(TestService.class, factoryBean.getObjectType());
    }

    @Test
    @DisplayName("测试 buildBaseUrl - 优先使用url")
    void testBuildBaseUrl_WithUrl() throws Exception {
        factoryBean.setUrl("http://test.com");
        factoryBean.afterPropertiesSet();

        // 使用反射调用私有方法
        java.lang.reflect.Method method = RemoteInterfaceFactoryBean.class.getDeclaredMethod("buildBaseUrl");
        method.setAccessible(true);
        String baseUrl = (String) method.invoke(factoryBean);

        assertEquals("http://test.com", baseUrl);
    }

    @Test
    @DisplayName("测试 buildBaseUrl - 使用serviceId")
    void testBuildBaseUrl_WithServiceId() throws Exception {
        factoryBean.setServiceId("test-service");
        factoryBean.afterPropertiesSet();

        java.lang.reflect.Method method = RemoteInterfaceFactoryBean.class.getDeclaredMethod("buildBaseUrl");
        method.setAccessible(true);
        String baseUrl = (String) method.invoke(factoryBean);

        assertEquals("http://test-service", baseUrl);
    }

    @Test
    @DisplayName("测试 getObject - 无fallback")
    void testGetObject_WithoutFallback() {
        factoryBean.setUrl("http://test.com");
        factoryBean.afterPropertiesSet();

        mockBuilderSelection();

        TestService proxy = factoryBean.getObject();

        assertNotNull(proxy);
        verify(applicationContext).getBean(
                CustomRestClientAutoConfiguration.REMOTE_REST_CLIENT_BUILDER_BEAN_NAME,
                RestClient.Builder.class
        );
        verifyNoInteractions(loadBalancerInterceptorProvider);
    }

    @Test
    @DisplayName("测试 getObject - serviceId追加负载均衡拦截器")
    void testGetObject_WithServiceIdUsesLoadBalancedBuilder() {
        factoryBean.setServiceId("test-service");
        factoryBean.afterPropertiesSet();

        mockBuilderSelection();
        when(applicationContext.getBeanProvider(DeferringLoadBalancerInterceptor.class))
                .thenReturn(loadBalancerInterceptorProvider);
        when(loadBalancerInterceptorProvider.getIfAvailable()).thenReturn(loadBalancerInterceptor);
        when(clonedRestClientBuilder.requestInterceptor(loadBalancerInterceptor))
                .thenReturn(clonedRestClientBuilder);

        TestService proxy = factoryBean.getObject();

        assertNotNull(proxy);
        verify(applicationContext).getBean(
                CustomRestClientAutoConfiguration.REMOTE_REST_CLIENT_BUILDER_BEAN_NAME,
                RestClient.Builder.class
        );
        verify(clonedRestClientBuilder).requestInterceptor(loadBalancerInterceptor);
    }

    @Test
    @DisplayName("测试 setter方法")
    void testSetters() {
        factoryBean.setType(TestService.class);
        factoryBean.setUrl("http://test.com");
        factoryBean.setServiceId("test-service");
        factoryBean.setFallbackFactory(RemoteInterfaceFallback.NoOp.class);

        assertEquals(TestService.class, factoryBean.getObjectType());
    }

    /**
     * 测试用的服务接口
     */
    private void mockBuilderSelection() {
        when(applicationContext.getBean(
                CustomRestClientAutoConfiguration.REMOTE_REST_CLIENT_BUILDER_BEAN_NAME,
                RestClient.Builder.class
        ))
                .thenReturn(sourceRestClientBuilder);
        when(sourceRestClientBuilder.clone()).thenReturn(clonedRestClientBuilder);
        when(clonedRestClientBuilder.baseUrl(anyString())).thenReturn(clonedRestClientBuilder);
        when(clonedRestClientBuilder.build()).thenReturn(restClient);
    }

    @HttpExchange("/test")
    interface TestService {

        @GetExchange
        String testMethod();

    }

}
