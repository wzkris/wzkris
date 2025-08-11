package com.wzkris.common.thread.refresh;

import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.wzkris.common.thread.properties.TpProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;

import java.util.concurrent.Executor;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * nacos支持
 */
@Slf4j
@ConditionalOnClass(value = NacosConfigManager.class)
public class NacosTpRefresher extends AbstractTpRefresher implements InitializingBean {

    private final ConfigService configService;

    protected NacosTpRefresher(NacosConfigManager nacosConfigManager) {
        this.configService = nacosConfigManager.getConfigService();
    }

    private void watchConfig() throws NacosException {
        final ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
        configService.addListener("thread.yml", "COMMON_GROUP", new Listener() {
            @Override
            public Executor getExecutor() {
                return new ThreadPoolExecutor(1, 1, 60, TimeUnit.SECONDS, new SynchronousQueue<>());
            }

            @Override
            public void receiveConfigInfo(String configInfo) {
                try {
                    TpProperties tpProperties = objectMapper.readValue(configInfo, TpProperties.class);
                    refreshTp(tpProperties);
                } catch (JsonProcessingException e) {
                    log.error("动态线程池配置转换异常：{}", e.getMessage());
                }
            }
        });
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        watchConfig();
    }

}
