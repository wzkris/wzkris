package com.wzkris.common.notifier.config;

import com.wzkris.common.notifier.client.DingtalkMsgClient;
import com.wzkris.common.notifier.core.impl.DingtalkNotifier;
import com.wzkris.common.notifier.properties.NotifierProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@ConditionalOnProperty(prefix = "notifier", name = "enabled", havingValue = "true")
public class DingtalkConfiguration {

    /**
     * 钉钉消息客户端
     */
    @Bean
    public DingtalkMsgClient dingtalkMsgClient() {
        return new DingtalkMsgClient();
    }

    /**
     * 钉钉通知器
     */
    @Bean
    @ConditionalOnProperty(prefix = "notifier", name = "channel", havingValue = "DINGTALK")
    public DingtalkNotifier dingtalkNotifier(DingtalkMsgClient dingtalkMsgClient, NotifierProperties notifierProperties) {
        return new DingtalkNotifier(dingtalkMsgClient, notifierProperties);
    }

}
