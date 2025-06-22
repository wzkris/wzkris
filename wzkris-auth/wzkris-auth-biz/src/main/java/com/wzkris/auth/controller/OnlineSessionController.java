package com.wzkris.auth.controller;

import com.wzkris.auth.domain.OnlineUser;
import com.wzkris.auth.service.TokenService;
import com.wzkris.common.core.constant.HeaderConstants;
import com.wzkris.common.core.domain.Result;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.security.utils.SystemUserUtil;
import com.wzkris.common.web.model.BaseController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RMapCache;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;

@Tag(name = "在线会话")
@Slf4j
@RestController
@RequestMapping("/online_session")
@RequiredArgsConstructor
public class OnlineSessionController extends BaseController {

    private final TokenService tokenService;

    @Operation(summary = "在线会话")
    @GetMapping("/sysuserinfo")
    public Result<Collection<OnlineUser>> onlineSession(HttpServletRequest request) {
        RMapCache<String, OnlineUser> onlineCache = tokenService.getOnlineCache(SystemUserUtil.getUserId());
        Collection<OnlineUser> onlineUsers = onlineCache.values();

        ArrayList<OnlineUser> list = new ArrayList<>(onlineUsers);
        String token = request.getHeader(HeaderConstants.X_TENANT_TOKEN);
        list.forEach(onlineUser -> {
            if (StringUtil.equals(onlineUser.getRefreshToken(), token)) {
                onlineUser.setCurrent(true);
            }
        });
        return ok(list);
    }

    @Operation(summary = "踢出会话")
    @PostMapping("/sysuserinfo/kickout")
    public Result<Void> kickoutSession(@RequestBody String refreshToken) {
        tokenService.kickoutOnlineSessionByRefreshToken(SystemUserUtil.getUserId(), refreshToken);
        tokenService.logoutByRefreshToken(refreshToken);
        return ok();
    }

}
