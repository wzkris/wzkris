package com.thingslink.auth.api;

import com.thingslink.auth.api.fallback.RemoteTokenApiFallback;
import com.thingslink.common.core.domain.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    @PostMapping(INNER_REQUEST_PATH + "/unlock/{username}")
    void unlockAccount(@PathVariable("username") String username);

    /**
     * 根据token获取用户信息
     *
     * @param token 可以是access_token/refresh_token/oidc_token 等等
     * @return 用户信息
     */
    @GetMapping(INNER_REQUEST_PATH + "/findByToken")
    Result<Object> findByToken(@RequestParam("token") String token);

    /**
     * 退出登录
     */
    @GetMapping(INNER_REQUEST_PATH + "/logout")
    void logoutByToken(@RequestParam("access_token") String accessToken);
}
