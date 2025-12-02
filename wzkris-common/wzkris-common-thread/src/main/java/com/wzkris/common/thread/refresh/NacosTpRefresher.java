package com.wzkris.common.thread.refresh;

import com.alibaba.cloud.nacos.refresh.NacosConfigRefreshEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.SmartApplicationListener;

import java.util.Optional;

/**
 * Nacos 配置刷新器
 * 监听 Nacos 配置刷新事件，触发 Tomcat 和业务线程池的刷新
 *
 * @author wzkris
 * @date 2025/12/02
 */
@Slf4j
@ConditionalOnClass(value = NacosConfigRefreshEvent.class)
public class NacosTpRefresher implements SmartApplicationListener {

    @Autowired(required = false)
    private TomcatTpRefresher tomcatTpRefresher;

    @Autowired(required = false)
    private BusinessTpRefresher businessTpRefresher;

    @Override
    public boolean supportsEventType(Class<? extends ApplicationEvent> eventType) {
        return NacosConfigRefreshEvent.class.isAssignableFrom(eventType);
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        // 刷新 Tomcat 线程池
        Optional.ofNullable(tomcatTpRefresher).ifPresent(TomcatTpRefresher::refresh);
        // 刷新业务线程池
        Optional.ofNullable(businessTpRefresher).ifPresent(BusinessTpRefresher::refresh);
    }

}
