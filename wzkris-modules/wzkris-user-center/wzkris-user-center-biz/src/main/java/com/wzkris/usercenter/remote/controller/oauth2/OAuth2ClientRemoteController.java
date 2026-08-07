package com.wzkris.usercenter.remote.controller.oauth2;

import com.wzkris.common.core.model.Result;
import com.wzkris.usercenter.remote.api.oauth2.OAuth2ClientRemoteApi;
import com.wzkris.usercenter.remote.api.oauth2.request.OAuth2ClientQueryRequest;
import com.wzkris.usercenter.remote.api.oauth2.response.OAuth2ClientListResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "OAuth2客户端")
@RestController
@RequestMapping("/oauth2-remote")
@RequiredArgsConstructor
public class OAuth2ClientRemoteController {

    private final OAuth2ClientRemoteApi oAuth2ClientRemoteApi;

    @Operation(summary = "查询OAuth2客户端列表")
    @PostMapping("/query-list")
    public Result<List<OAuth2ClientListResponse>> queryList(@RequestBody @Valid OAuth2ClientQueryRequest request) {
        return oAuth2ClientRemoteApi.queryList(request);
    }

}
