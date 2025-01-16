package com.wzkris.equipment.api;

import com.wzkris.common.core.constant.ApplicationNameConstants;
import com.wzkris.equipment.api.fallback.RemoteDeviceApiFallback;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = ApplicationNameConstants.EQUIPMENT, contextId = "RemoteDeviceApi", fallbackFactory = RemoteDeviceApiFallback.class)
public interface RemoteDeviceApi {

}
