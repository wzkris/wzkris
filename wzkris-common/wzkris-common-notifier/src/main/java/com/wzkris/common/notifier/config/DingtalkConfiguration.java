package com.wzkris.common.notifier.config;

import com.wzkris.common.notifier.client.DingtalkMsgClient;
import com.wzkris.common.notifier.core.impl.DingtalkNotifier;
import com.wzkris.common.notifier.properties.NotifierProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@ConditionalOnProperty(prefix = "notifier", name = "enabled", havingValue = "true")
public class DingtalkConfiguration {

    /**
     * 钉钉 API 客户端
     */
    @Bean
    @ConditionalOnProperty(prefix = "notifier", name = "channel", havingValue = "DINGTALK")
    public DingtalkMsgClient dingtalkMsgClient(NotifierProperties notifierProperties) {
        if (!org.springframework.util.StringUtils.hasText(notifierProperties.getDingtalk().getRobotWebhook())) {
            throw new IllegalStateException("钉钉机器人配置缺失：robotWebhook 不能为空");
        }
        log.info("初始化钉钉API客户端");
        return new DingtalkMsgClient(notifierProperties.getDingtalk().getRobotWebhook());
    }

    /**
     * 钉钉通知器
     */
    @Bean
    @ConditionalOnBean(DingtalkMsgClient.class)
    public DingtalkNotifier dingtalkNotifier(DingtalkMsgClient dingtalkMsgClient) {
        return new DingtalkNotifier(dingtalkMsgClient);
    }

}
