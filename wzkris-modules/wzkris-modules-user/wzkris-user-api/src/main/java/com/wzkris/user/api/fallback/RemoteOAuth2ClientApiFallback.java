package com.wzkris.user.api.fallback;

import com.wzkris.common.core.domain.Result;
import com.wzkris.common.core.enums.BizCode;
import com.wzkris.user.api.RemoteOAuth2ClientApi;
import com.wzkris.user.api.domain.dto.OAuth2ClientDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import static com.wzkris.common.core.domain.Result.resp;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : Auth2客户端API降级
 * @date : 2024/7/3 14:37
 */
@Slf4j
@Component
public class RemoteOAuth2ClientApiFallback implements FallbackFactory<RemoteOAuth2ClientApi> {
    @Override
    public RemoteOAuth2ClientApi create(Throwable cause) {
        return new RemoteOAuth2ClientApi() {
            @Override
            public Result<OAuth2ClientDTO> getByClientId(String clientid) {
                log.error("查询oauth2客户端信息发生异常，errMsg：{}", cause.getMessage(), cause);
                return resp(BizCode.RPC_INVOCATION, cause.getMessage());
            }
        };
    }
}
