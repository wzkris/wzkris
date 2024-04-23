package com.thingslink.equipment.api;

import com.thingslink.equipment.api.fallback.RemoteDeviceApiFallback;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "thingslink-equipment", contextId = "RemoteDeviceApi", fallbackFactory = RemoteDeviceApiFallback.class)
public interface RemoteDeviceApi {
}
