package com.wzkris.usercenter.remote.controller.oauth2;

import com.wzkris.common.core.model.Result;
import com.wzkris.usercenter.remote.api.oauth2.OAuth2ClientRemoteApi;
import com.wzkris.usercenter.remote.api.oauth2.response.OAuth2ClientResponse;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Hidden
@RestController
@RequestMapping("/oauth2-remote")
@RequiredArgsConstructor
public class OAuth2ClientRemoteController {

    private final OAuth2ClientRemoteApi oAuth2ClientRemoteApi;

    @PostMapping("/query-by-id")
    public Result<OAuth2ClientResponse> queryById(@RequestBody String id) {
        return oAuth2ClientRemoteApi.queryById(id);
    }

    @PostMapping("/query-by-clientid")
    public Result<OAuth2ClientResponse> queryByClientId(@RequestBody String clientid) {
        return oAuth2ClientRemoteApi.queryByClientId(clientid);
    }

}





