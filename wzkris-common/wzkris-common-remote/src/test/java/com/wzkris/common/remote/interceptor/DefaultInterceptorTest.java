package com.wzkris.common.remote.interceptor;

import com.wzkris.common.core.constant.CustomHeaderConstants;
import com.wzkris.common.remote.event.RemoteCallEvent;
import com.wzkris.common.remote.properties.RemoteInterfaceProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.mock.http.client.MockClientHttpRequest;
import org.springframework.mock.http.client.MockClientHttpResponse;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("DefaultInterceptor 单测")
class DefaultInterceptorTest {

    @Mock
    private ApplicationEventPublisher publisher;

    @Mock
    private ClientHttpRequestExecution execution;

    private RemoteInterfaceProperties remoteInterfaceProperties;

    private DefaultInterceptor interceptor;

    @BeforeEach
    void setUp() {
        remoteInterfaceProperties = new RemoteInterfaceProperties();
        interceptor = new DefaultInterceptor(publisher, remoteInterfaceProperties);
    }

    @Test
    @DisplayName("非法请求时间头不应中断成功调用")
    void testIntercept_WithInvalidRequestTimeHeader() throws IOException {
        MockClientHttpRequest request = new MockClientHttpRequest(HttpMethod.POST, URI.create("http://test/api"));
        request.getHeaders().add(CustomHeaderConstants.X_REQUEST_TIME, "not-a-number");
        MockClientHttpResponse response = new MockClientHttpResponse(
                "{\"ok\":true}".getBytes(StandardCharsets.UTF_8),
                HttpStatus.OK
        );
        when(execution.execute(any(), any())).thenReturn(response);

        ClientHttpResponse actualResponse = interceptor.intercept(
                request,
                "{\"req\":1}".getBytes(StandardCharsets.UTF_8),
                execution
        );

        assertSame(response, actualResponse);

        ArgumentCaptor<RemoteCallEvent> eventCaptor = ArgumentCaptor.forClass(RemoteCallEvent.class);
        verify(publisher).publishEvent(eventCaptor.capture());
        RemoteCallEvent event = eventCaptor.getValue();
        assertEquals("POST", event.getHttpMethod());
        assertEquals("http://test/api", event.getHttpUri());
        assertEquals(-1L, event.getCostTime());
        assertEquals(HttpStatus.OK.value(), event.getHttpStatusCode());
    }

    @Test
    @DisplayName("下游抛出异常时应继续上抛并发布失败事件")
    void testIntercept_WhenExecutionFails() throws IOException {
        MockClientHttpRequest request = new MockClientHttpRequest(HttpMethod.GET, URI.create("http://test/fail"));
        IOException downstreamException = new IOException("downstream unavailable");
        when(execution.execute(any(), any())).thenThrow(downstreamException);

        IOException actualException = assertThrows(IOException.class, () -> interceptor.intercept(
                request,
                new byte[0],
                execution
        ));

        assertSame(downstreamException, actualException);

        ArgumentCaptor<RemoteCallEvent> eventCaptor = ArgumentCaptor.forClass(RemoteCallEvent.class);
        verify(publisher).publishEvent(eventCaptor.capture());
        RemoteCallEvent event = eventCaptor.getValue();
        assertEquals("GET", event.getHttpMethod());
        assertEquals("http://test/fail", event.getHttpUri());
        assertTrue(event.getErrorMessage().contains("downstream unavailable"));
    }

    @Test
    @DisplayName("事件发布失败不应反向影响主调用")
    void testIntercept_WhenPublishFails() throws IOException {
        MockClientHttpRequest request = new MockClientHttpRequest(HttpMethod.GET, URI.create("http://test/publish"));
        MockClientHttpResponse response = new MockClientHttpResponse(new byte[0], HttpStatus.OK);
        when(execution.execute(any(), any())).thenReturn(response);
        doThrow(new IllegalStateException("publisher error"))
                .when(publisher)
                .publishEvent(any(Object.class));

        assertDoesNotThrow(() -> interceptor.intercept(request, new byte[0], execution));
    }

    @Test
    @DisplayName("关闭事件发布后不应发送RemoteCallEvent")
    void testIntercept_WhenPublishEventDisabled() throws IOException {
        interceptor = new DefaultInterceptor(publisher, remoteInterfaceProperties);

        MockClientHttpRequest request = new MockClientHttpRequest(HttpMethod.GET, URI.create("http://test/no-event"));
        MockClientHttpResponse response = new MockClientHttpResponse(new byte[0], HttpStatus.OK);
        when(execution.execute(any(), any())).thenReturn(response);

        assertDoesNotThrow(() -> interceptor.intercept(request, new byte[0], execution));

        verifyNoInteractions(publisher);
    }

    @Test
    @DisplayName("配置最大body长度后应对事件中的body截断")
    void testIntercept_WhenBodyLengthLimited() throws IOException {
        remoteInterfaceProperties.getObservation().setMaxBodyLength(4);
        interceptor = new DefaultInterceptor(publisher, remoteInterfaceProperties);

        MockClientHttpRequest request = new MockClientHttpRequest(HttpMethod.POST, URI.create("http://test/trim"));
        MockClientHttpResponse response = new MockClientHttpResponse(
                "123456".getBytes(StandardCharsets.UTF_8),
                HttpStatus.OK
        );
        when(execution.execute(any(), any())).thenReturn(response);

        interceptor.intercept(
                request,
                "abcdef".getBytes(StandardCharsets.UTF_8),
                execution
        );

        ArgumentCaptor<RemoteCallEvent> eventCaptor = ArgumentCaptor.forClass(RemoteCallEvent.class);
        verify(publisher).publishEvent(eventCaptor.capture());
        RemoteCallEvent event = eventCaptor.getValue();
        assertEquals("abcd...(truncated)", event.getRequestBody());
        assertEquals("1234...(truncated)", event.getResponseBody());
    }

}
