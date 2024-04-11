package com.thingslink.user.api;

import com.thingslink.common.core.domain.Result;
import com.thingslink.user.api.domain.dto.Oauth2ClientDTO;
import com.thingslink.user.api.fallback.RemoteOAuth2ClientApiFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static com.thingslink.common.core.constant.SecurityConstants.INNER_REQUEST_PATH;


/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : rpc - OAuth2客户端
 * @date : 2024/4/15 16:20
 */
@FeignClient(value = "thingslink-user", contextId = "RemoteOAuth2ClientApi",
        fallbackFactory = RemoteOAuth2ClientApiFallback.class)
public interface RemoteOAuth2ClientApi {

    /**
     * 查询OAuth2客户端
     */
    @GetMapping(INNER_REQUEST_PATH + "/query_client_by_clientid")
    Result<Oauth2ClientDTO> getByClientId(@RequestParam("clientId") String clientId);
}
