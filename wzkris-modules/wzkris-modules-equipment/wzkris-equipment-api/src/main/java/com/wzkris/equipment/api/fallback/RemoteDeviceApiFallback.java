package com.wzkris.equipment.api.fallback;

import com.wzkris.equipment.api.RemoteDeviceApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RemoteDeviceApiFallback implements FallbackFactory<RemoteDeviceApi> {

    @Override
    public RemoteDeviceApi create(Throwable cause) {
        return new RemoteDeviceApi() {
        };
    }

}
