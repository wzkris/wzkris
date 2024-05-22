package com.thingslink.auth.api.fallback;

import com.thingslink.auth.api.RemoteTokenApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 降级
 * @date : 2023/8/5 15:32
 */
@Slf4j
@Component
public class RemoteTokenApiFallback implements FallbackFactory<RemoteTokenApi> {
    @Override
    public RemoteTokenApi create(Throwable cause) {
        log.error("-----------认证服务发生熔断-----------");
        return new RemoteTokenApi() {
            @Override
            public void unlockAccount(String username) {
                log.error("解锁账户发生异常，errMsg：{}", cause.getMessage());
            }

        };
    }
}
