package com.wzkris.common.thread.adapter;

import com.wzkris.common.thread.properties.TpProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.AbstractProtocol;
import org.apache.coyote.ProtocolHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.web.embedded.tomcat.TomcatWebServer;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;

@Slf4j
@ConditionalOnClass(TomcatWebServer.class)
public class TomcatTpAdapter extends WebServerTpAdapter {

    public TomcatTpAdapter(ServletWebServerApplicationContext servletWebServerApplicationContext) {
        super(servletWebServerApplicationContext);
    }

    @Override
    public void refreshWeb(TpProperties.TomcatThreadProperties properties) {
        if (properties == null || properties.isEmpty()) {
            return;
        }
        ProtocolHandler protocolHandler = ((TomcatWebServer) webServer).getTomcat().getConnector().getProtocolHandler();
        if (!(protocolHandler instanceof AbstractProtocol<?> protocol)) {
            log.warn("当前 ProtocolHandler 类型不支持动态线程参数刷新: {}", protocolHandler.getClass().getName());
            return;
        }

        boolean changed = false;

        if (properties.getMaxThreads() != null) {
            protocol.setMaxThreads(properties.getMaxThreads());
            changed = true;
        }
        if (properties.getMinSpareThreads() != null) {
            protocol.setMinSpareThreads(properties.getMinSpareThreads());
            changed = true;
        }
        if (properties.getMaxConnections() != null) {
            protocol.setMaxConnections(properties.getMaxConnections());
            changed = true;
        }
        if (properties.getAcceptCount() != null) {
            protocol.setAcceptCount(properties.getAcceptCount());
            changed = true;
        }
        if (properties.getKeepAliveTimeout() != null) {
            protocol.setKeepAliveTimeout(properties.getKeepAliveTimeout());
            changed = true;
        }

        if (changed) {
            log.info("Tomcat 线程参数刷新成功: {}", properties);
        }
    }

}
