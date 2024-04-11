package com.thingslink.auth.api.fallback;

import com.thingslink.auth.api.RemoteTokenApi;
import com.thingslink.common.core.domain.Result;
import com.thingslink.common.core.enums.BizCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import static com.thingslink.common.core.domain.Result.resp;

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

            @Override
            public Result<Object> findByToken(String token) {
                log.error("根据access_token查找用户异常，errMsg：{}", cause.getMessage());
                return resp(BizCode.RPC_INVOCATION, cause.getMessage());
            }

            @Override
            public void logoutByToken(String accessToken) {
                log.error("用户退出登录发生异常，errMsg：{}", cause.getMessage());
            }

        };
    }
}
