package com.wzkris.system.listener;

import com.wzkris.system.listener.event.RefreshConfigEvent;
import com.wzkris.system.service.SysConfigService;
import com.wzkris.system.utils.ConfigCacheUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 字典事件监听
 * @date : 2024/11/20 15:10
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RefreshConfigListener implements CommandLineRunner {

    private final SysConfigService configService;

    /**
     * 异步刷新缓存
     */
    @Async
    @EventListener
    public void loginEvent(RefreshConfigEvent configEvent) {
        ConfigCacheUtil.setKey(configEvent.getKey(), configEvent.getValue());
    }

    @Override
    public void run(String... args) throws Exception {
        configService.loadingConfigCache();
    }
}
