package com.wzkris.common.thread.refresh;

import com.alibaba.cloud.nacos.refresh.NacosConfigRefreshEvent;
import com.wzkris.common.thread.properties.TpProperties;
import com.wzkris.common.thread.refresh.adapter.WebServerTpAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.SmartApplicationListener;

/**
 * nacos支持
 */
@Slf4j
@ConditionalOnClass(value = NacosConfigRefreshEvent.class)
public class NacosTpRefresher extends AbstractTpRefresher implements SmartApplicationListener {

    public NacosTpRefresher(TpProperties tpProperties, WebServerTpAdapter webServerTpAdapter) {
        super(tpProperties, webServerTpAdapter);
    }

    @Override
    public boolean supportsEventType(Class<? extends ApplicationEvent> eventType) {
        return NacosConfigRefreshEvent.class.isAssignableFrom(eventType);
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        refreshTp();
    }

}
