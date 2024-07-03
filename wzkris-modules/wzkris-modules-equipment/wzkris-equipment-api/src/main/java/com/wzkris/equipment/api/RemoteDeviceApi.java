package com.wzkris.equipment.api;

import com.wzkris.equipment.api.fallback.RemoteDeviceApiFallback;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "wzkris-equipment", contextId = "RemoteDeviceApi", fallbackFactory = RemoteDeviceApiFallback.class)
public interface RemoteDeviceApi {
}
