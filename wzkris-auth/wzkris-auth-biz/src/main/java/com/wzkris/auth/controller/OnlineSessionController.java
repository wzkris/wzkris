package com.wzkris.auth.controller;

import com.wzkris.auth.domain.OnlineSession;
import com.wzkris.auth.domain.resp.OnlineSessionResp;
import com.wzkris.auth.service.TokenService;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.security.utils.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RMapCache;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static com.wzkris.common.core.model.Result.ok;

@Tag(name = "在线会话")
@Slf4j
@RestController
@RequestMapping("/online-session")
@RequiredArgsConstructor
public class OnlineSessionController {

    private final TokenService tokenService;

    @Operation(summary = "在线会话")
    @GetMapping
    public Result<Collection<OnlineSessionResp>> onlineSession() {
        String accessToken = SecurityUtil.getTokenValue();
        String refreshToken = tokenService.loadRefreshTokenByAccessToken(accessToken);

        RMapCache<String, OnlineSession> onlineCache = tokenService.getOnlineCache(SecurityUtil.getId());

        List<OnlineSessionResp> resps = new ArrayList<>();
        for (Map.Entry<String, OnlineSession> entry : onlineCache.entrySet()) {
            OnlineSessionResp userResp = new OnlineSessionResp(entry.getValue());
            userResp.setRefreshToken(entry.getKey());
            if (StringUtil.equals(refreshToken, entry.getKey())) {
                userResp.setCurrent(true);
            }
            resps.add(userResp);
        }

        return ok(resps);
    }

    @Operation(summary = "踢出会话")
    @PostMapping("/kickout")
    public Result<Void> kickoutSession(@RequestBody String refreshToken) {
        tokenService.logoutByRefreshToken(refreshToken);
        return ok();
    }

}
