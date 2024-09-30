package com.wzkris.auth.api.fallback;

import com.wzkris.auth.api.RemoteTokenApi;
import com.wzkris.auth.api.domain.ReqToken;
import com.wzkris.common.core.domain.Result;
import com.wzkris.common.core.enums.BizCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.wzkris.common.core.domain.Result.resp;

@Slf4j
@Component
public class RemoteTokenApiFallback implements FallbackFactory<RemoteTokenApi> {
    @Override
    public RemoteTokenApi create(Throwable cause) {
        log.error("-----------认证服务发生熔断-----------");
        return new RemoteTokenApi() {
            @Override
            public Result<String> getTokenReqId(String token) {
                log.error("获取token请求ID发生异常，errMsg：{}", cause.getMessage());

                return resp(BizCode.RPC_INVOCATION, cause.getMessage());
            }

            @Override
            public Result<Map<String, Object>> checkToken(ReqToken reqToken) {
                log.error("验证token发生异常，errMsg：{}", cause.getMessage());

                return resp(BizCode.RPC_INVOCATION, cause.getMessage());
            }
        };
    }
}
