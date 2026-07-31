package com.wzkris.usercenter.remote.controller.oauth2;

import com.wzkris.common.core.model.Result;
import com.wzkris.usercenter.remote.api.oauth2.OAuth2ClientRemoteApi;
import com.wzkris.usercenter.remote.api.oauth2.request.OAuth2ClientQueryRequest;
import com.wzkris.usercenter.remote.api.oauth2.response.OAuth2ClientResponse;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Hidden
@RestController
@RequestMapping("/oauth2-remote")
@RequiredArgsConstructor
public class OAuth2ClientRemoteController {

    private final OAuth2ClientRemoteApi oAuth2ClientRemoteApi;

    @PostMapping("/query-list")
    public Result<List<OAuth2ClientResponse>> queryList(@RequestBody @Valid OAuth2ClientQueryRequest request) {
        return oAuth2ClientRemoteApi.queryList(request);
    }

}

