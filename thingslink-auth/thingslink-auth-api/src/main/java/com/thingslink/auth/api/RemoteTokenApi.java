package com.thingslink.auth.api;

import com.thingslink.auth.api.fallback.RemoteTokenApiFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import static com.thingslink.common.core.constant.SecurityConstants.INNER_REQUEST_PATH;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : rpc - 认证服务
 * @date : 2023/8/5 15:32
 */
@FeignClient(value = "thingslink-auth", contextId = "RemoteTokenApi", fallbackFactory = RemoteTokenApiFallback.class)
public interface RemoteTokenApi {

    // 解锁账户
    @PostMapping(INNER_REQUEST_PATH + "/unlock_account")
    void unlockAccount(@RequestBody String username);

}
