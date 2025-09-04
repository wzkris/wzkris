package com.wzkris.common.thread.refresh.adapter;

import com.wzkris.common.thread.properties.ExecutorProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.ProtocolHandler;
import org.apache.tomcat.util.threads.ThreadPoolExecutor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.web.embedded.tomcat.TomcatWebServer;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;

@Slf4j
@ConditionalOnClass(TomcatWebServer.class)
public class TomcatTpAdapter extends WebServerTpAdapter {

    public TomcatTpAdapter(ServletWebServerApplicationContext servletWebServerApplicationContext) {
        super(servletWebServerApplicationContext);
    }

    @Override
    public void refreshWeb(ExecutorProperties properties) {
        if (properties == null) {
            return;
        }
        TomcatWebServer tomcatWebServer = (TomcatWebServer) webServer;
        ProtocolHandler protocolHandler = tomcatWebServer.getTomcat().getConnector().getProtocolHandler();

        Executor oldExecutor = protocolHandler.getExecutor();

        protocolHandler.setExecutor(new ThreadPoolExecutor(properties.getCorePoolSize(), properties.getMaximumPoolSize(),
                properties.getKeepAliveTime(), properties.getUnit(), new LinkedBlockingQueue<>(properties.getQueueCapacity()),
                new ThreadPoolExecutor.AbortPolicy()));
        log.info("{}", properties);

        ((ThreadPoolExecutor) oldExecutor).shutdown();
    }

}
