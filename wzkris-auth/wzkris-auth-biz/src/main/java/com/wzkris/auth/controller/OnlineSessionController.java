package com.wzkris.auth.controller;

import com.wzkris.auth.domain.OnlineUser;
import com.wzkris.auth.oauth2.redis.entity.OAuth2AuthorizationGrantAuthorization;
import com.wzkris.auth.oauth2.redis.repository.OAuth2AuthorizationGrantAuthorizationRepository;
import com.wzkris.auth.utils.OnlineUserUtil;
import com.wzkris.common.core.domain.Result;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.security.utils.LoginUtil;
import com.wzkris.common.web.model.BaseController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RMapCache;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;

@Tag(name = "在线会话")
@Slf4j
@RestController
@RequestMapping("/online_session")
@RequiredArgsConstructor
public class OnlineSessionController extends BaseController {

    private final OAuth2AuthorizationGrantAuthorizationRepository authorizationRepository;

    @Operation(summary = "在线会话")
    @GetMapping("/sysuserinfo")
    public Result<Collection<OnlineUser>> onlineSession() {
        RMapCache<String, OnlineUser> onlineCache = OnlineUserUtil.getOnlineCache(LoginUtil.getUserId());
        Collection<OnlineUser> onlineUsers = onlineCache.values();

        ArrayList<OnlineUser> list = new ArrayList<>(onlineUsers);
        OAuth2AccessToken accessToken = LoginUtil.getAuthentication().getToken();
        OAuth2AuthorizationGrantAuthorization grantAuthorization = authorizationRepository
                .findByAccessToken_TokenValue(accessToken.getTokenValue());
        list.forEach(onlineUser -> {
            if (StringUtil.equals(onlineUser.getTokenId(), grantAuthorization.getId())) {
                onlineUser.setCurrent(true);
            }
        });
        return ok(list);
    }

    @Operation(summary = "踢出会话")
    @PostMapping("/sysuserinfo/kickout")
    public Result<Void> kickoutSession(@RequestBody String tokenId) {
        RMapCache<String, OnlineUser> onlineCache = OnlineUserUtil.getOnlineCache(LoginUtil.getUserId());

        boolean success = onlineCache.remove(tokenId) != null;
        if (success) {
            authorizationRepository.deleteById(tokenId);
        }

        return toRes(success);
    }

}
