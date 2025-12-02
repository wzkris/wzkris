package com.wzkris.common.thread.adapter;

import com.wzkris.common.thread.properties.TpProperties;
import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.server.WebServer;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;

/**
 * web容器线程池适配
 *
 * @author wzkris
 * @date 2025/8/13
 */
@Slf4j
public abstract class WebServerTpAdapter {

    protected final WebServer webServer;

    public WebServerTpAdapter(ServletWebServerApplicationContext servletWebServerApplicationContext) {
        this.webServer = servletWebServerApplicationContext.getWebServer();
    }

    public void refreshWeb(@Nullable TpProperties.TomcatThreadProperties properties) {
        // empty
    }

}
