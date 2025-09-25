package com.wzkris.auth.controller;

import com.wzkris.auth.security.oauth2.redis.repository.OAuth2RegisteredClientRepository;
import com.wzkris.common.core.model.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.wzkris.common.core.model.Result.ok;

@Tag(name = "刷新OAuth2客户端缓存")
@Slf4j
@Controller
@RestController
@RequiredArgsConstructor
public class RefreshController {

    private final OAuth2RegisteredClientRepository registeredClientRepository;

    @Operation(summary = "刷新缓存")
    @PostMapping("/refresh-client")
    public Result<Void> refresh_client(@RequestBody String clientId) {
        registeredClientRepository.deleteById(clientId);
        return ok();
    }

}
