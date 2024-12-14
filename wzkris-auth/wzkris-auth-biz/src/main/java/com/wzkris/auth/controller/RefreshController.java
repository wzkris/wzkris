package com.wzkris.auth.controller;

import com.wzkris.auth.oauth2.service.RedisRegisteredClientRepository;
import com.wzkris.common.core.domain.Result;
import com.wzkris.common.web.model.BaseController;
import com.wzkris.user.api.RemoteOAuth2ClientApi;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "刷新OAuth2客户端缓存")
@Slf4j
@Controller
@RestController
@RequiredArgsConstructor
public class RefreshController extends BaseController {

    @Autowired
    private RemoteOAuth2ClientApi remoteOAuth2ClientApi;

    @Autowired
    private RedisRegisteredClientRepository registeredClientRepository;

    @Operation(summary = "刷新缓存")
    @PostMapping("/refresh_client")
    public Result<Void> logout(@RequestBody String clientId) {
        registeredClientRepository.remove(clientId);
        return ok();
    }
}
