package com.thingslink.user.api.fallback;

import com.thingslink.common.core.domain.Result;
import com.thingslink.common.core.enums.BizCode;
import com.thingslink.user.api.RemoteOAuth2ClientApi;
import com.thingslink.user.api.domain.dto.Oauth2ClientDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import static com.thingslink.common.core.domain.Result.resp;


/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : OAuth2客户端API降级
 * @date : 2023/8/21 13:03
 */
@Slf4j
@Component
public class RemoteOAuth2ClientApiFallback implements FallbackFactory<RemoteOAuth2ClientApi> {
    @Override
    public RemoteOAuth2ClientApi create(Throwable cause) {
        return new RemoteOAuth2ClientApi() {
            @Override
            public Result<Oauth2ClientDTO> getByClientId(String clientId) {
                log.error("查询OAuth2客户端信息发生异常，errMsg：{}", cause.getMessage(), cause);
                return resp(BizCode.RPC_INVOCATION, cause.getMessage());
            }
        };
    }
}
