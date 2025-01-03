package com.wzkris.user.api;

import com.wzkris.common.core.constant.ApplicationNameConstants;
import com.wzkris.common.core.domain.Result;
import com.wzkris.user.api.domain.response.OAuth2ClientResp;
import com.wzkris.user.api.fallback.RemoteOAuth2ClientApiFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static com.wzkris.common.core.constant.SecurityConstants.INNER_NOAUTH_REQUEST_PATH;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : rpc - OAuth2客户端
 * @date : 2024/7/3 14:37
 */
@FeignClient(value = ApplicationNameConstants.USER, contextId = "RemoteOAuth2ClientApi",
        fallbackFactory = RemoteOAuth2ClientApiFallback.class)
public interface RemoteOAuth2ClientApi {

    /**
     * 根据clientid查询客户端信息
     *
     * @param clientid clientid
     * @return oauth2客户端
     */
    @GetMapping(INNER_NOAUTH_REQUEST_PATH + "/query_oauth2_client_by_clientid")
    Result<OAuth2ClientResp> getByClientId(@RequestParam("clientid") String clientid);

}
