package com.wzkris.auth.controller;

import com.wzkris.auth.oauth2.redis.repository.OAuth2RegisteredClientRepository;
import com.wzkris.common.core.domain.Result;
import com.wzkris.common.web.model.BaseController;
import com.wzkris.user.rmi.RmiOAuth2ClientService;
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
    private RmiOAuth2ClientService rmiOAuth2ClientService;

    @Autowired
    private OAuth2RegisteredClientRepository registeredClientRepository;

    @Operation(summary = "刷新缓存")
    @PostMapping("/refresh_client")
    public Result<Void> refresh_client(@RequestBody String clientId) {
        registeredClientRepository.deleteById(clientId);
        return ok();
    }
}
