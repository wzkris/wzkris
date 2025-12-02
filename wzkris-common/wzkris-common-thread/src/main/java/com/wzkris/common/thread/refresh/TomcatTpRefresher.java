package com.wzkris.common.thread.refresh;

import com.wzkris.common.thread.adapter.WebServerTpAdapter;
import com.wzkris.common.thread.properties.TpProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import java.util.Objects;

/**
 * Tomcat 线程池刷新器
 * 专门处理 Tomcat 线程池的动态刷新逻辑
 *
 * @author wzkris
 * @date 2025/12/02
 */
@Slf4j
public class TomcatTpRefresher implements ApplicationRunner {

    private final TpProperties tpProperties;

    private final WebServerTpAdapter webServerTpAdapter;

    private TpProperties.TomcatThreadProperties localTomcatProperties;

    public TomcatTpRefresher(TpProperties tpProperties, WebServerTpAdapter webServerTpAdapter) {
        this.tpProperties = tpProperties;
        this.webServerTpAdapter = webServerTpAdapter;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        refresh();
    }

    /**
     * 刷新 Tomcat 线程池配置
     */
    public void refresh() {
        TpProperties.TomcatThreadProperties remote = tpProperties.getTomcatExecutor();
        if (remote == null || remote.isEmpty()) {
            return;
        }

        if (hasChanges(remote)) {
            log.info("Tomcat 线程参数发生变化，开始刷新");
            webServerTpAdapter.refreshWeb(remote);
            updateLocalConfiguration(remote);
        }
    }

    private boolean hasChanges(TpProperties.TomcatThreadProperties remote) {
        if (localTomcatProperties == null) {
            return true;
        }
        return !Objects.equals(localTomcatProperties.getMaxThreads(), remote.getMaxThreads())
                || !Objects.equals(localTomcatProperties.getMinSpareThreads(), remote.getMinSpareThreads())
                || !Objects.equals(localTomcatProperties.getMaxConnections(), remote.getMaxConnections())
                || !Objects.equals(localTomcatProperties.getAcceptCount(), remote.getAcceptCount())
                || !Objects.equals(localTomcatProperties.getKeepAliveTimeout(), remote.getKeepAliveTimeout());
    }

    /**
     * 更新本地配置
     */
    private void updateLocalConfiguration(TpProperties.TomcatThreadProperties source) {
        if (localTomcatProperties == null) {
            localTomcatProperties = new TpProperties.TomcatThreadProperties();
        }
        localTomcatProperties.setMaxThreads(source.getMaxThreads());
        localTomcatProperties.setMinSpareThreads(source.getMinSpareThreads());
        localTomcatProperties.setMaxConnections(source.getMaxConnections());
        localTomcatProperties.setAcceptCount(source.getAcceptCount());
        localTomcatProperties.setKeepAliveTimeout(source.getKeepAliveTimeout());
    }

}

