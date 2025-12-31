package com.wzkris.common.notifier.config;

import com.wzkris.common.notifier.core.impl.DingtalkNotifier;
import com.wzkris.common.notifier.dingtalk.client.DingtalkApiClient;
import com.wzkris.common.notifier.properties.DingtalkProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@EnableConfigurationProperties(DingtalkProperties.class)
@ConditionalOnProperty(prefix = "dingtalk", name = "enabled", havingValue = "true")
public class DingtalkConfiguration {

    /**
     * 钉钉 API 客户端
     */
    @Bean
    public DingtalkApiClient dingtalkApiClient(DingtalkProperties dingtalkProperties) {
        if (!org.springframework.util.StringUtils.hasText(dingtalkProperties.getAppKey())
                || !org.springframework.util.StringUtils.hasText(dingtalkProperties.getAppSecret())
                || !org.springframework.util.StringUtils.hasText(dingtalkProperties.getRobotCode())) {
            throw new IllegalStateException("钉钉配置缺失：appKey/appSecret/robotCode 不能为空");
        }
        log.info("初始化钉钉API客户端");
        return new DingtalkApiClient(dingtalkProperties);
    }

    /**
     * 钉钉通知器
     */
    @Bean
    public DingtalkNotifier dingtalkNotifier(DingtalkApiClient apiClient) {
        return new DingtalkNotifier(apiClient);
    }

}
