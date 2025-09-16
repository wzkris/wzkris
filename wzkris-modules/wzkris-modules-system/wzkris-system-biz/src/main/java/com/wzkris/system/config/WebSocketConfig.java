package com.wzkris.system.config;

import com.wzkris.system.websocket.AuthHandShakeInterceptor;
import com.wzkris.system.websocket.BinaryProtocolWebSocketHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {

    private final BinaryProtocolWebSocketHandler binaryProtocolWebSocketHandler;

    private final AuthHandShakeInterceptor authHandShakeInterceptor;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(binaryProtocolWebSocketHandler, "/ws/message")
                .addInterceptors(authHandShakeInterceptor, new HttpSessionHandshakeInterceptor())
                .setAllowedOriginPatterns("*");

    }

    @Bean
    public ServletServerContainerFactoryBean createWebSocketContainer() {
        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
        container.setMaxTextMessageBufferSize(8192); // 8KB
        container.setMaxBinaryMessageBufferSize(8192); // 8KB
        container.setMaxSessionIdleTimeout(300_000L); // 5分钟
        container.setAsyncSendTimeout(10_000L); // 10秒发送超时
        return container;
    }

}
