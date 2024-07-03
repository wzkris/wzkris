package com.wzkris.user.api;

import com.wzkris.common.core.domain.Result;
import com.wzkris.user.api.domain.dto.OAuth2ClientDTO;
import com.wzkris.user.api.fallback.RemoteOAuth2ClientApiFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static com.wzkris.common.core.constant.SecurityConstants.INNER_REQUEST_PATH;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : rpc - OAuth2客户端
 * @date : 2024/7/3 14:37
 */
@FeignClient(value = "wzkris-user", contextId = "RemoteOAuth2ClientApi",
        fallbackFactory = RemoteOAuth2ClientApiFallback.class)
public interface RemoteOAuth2ClientApi {

    /**
     * 根据手机号查询app用户
     */
    @GetMapping(INNER_REQUEST_PATH + "/query_oauth2_client_by_clientid")
    Result<OAuth2ClientDTO> getByClientId(@RequestParam("clientid") String clientid);

}
