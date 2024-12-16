package com.wzkris.system.api.fallback;

import com.wzkris.system.api.RemoteNotifyApi;
import com.wzkris.system.api.domain.request.SendNotifyReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 降级
 * @date : 2024/12/16 12:50
 */
@Slf4j
@Component
public class RemoteNotifyApiFallback implements FallbackFactory<RemoteNotifyApi> {

    @Override
    public RemoteNotifyApi create(Throwable cause) {
        log.error("-------openfeign触发熔断，系统服务调用失败-------");
        return new RemoteNotifyApi() {
            @Override
            public void sendSystemNotify(SendNotifyReq sendNotifyReq) {
                log.error("发送系统通知发生异常，errMsg：{}", cause.getMessage(), cause);
            }

            @Override
            public void sendDeviceNotify(SendNotifyReq sendNotifyReq) {
                log.error("发送设备通知发生异常，errMsg：{}", cause.getMessage(), cause);
            }
        };
    }
}
