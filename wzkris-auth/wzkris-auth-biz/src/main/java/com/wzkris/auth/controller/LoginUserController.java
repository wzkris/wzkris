package com.wzkris.auth.controller;


import com.wzkris.auth.config.TokenConfig;
import com.wzkris.auth.controller.hooks.OAuth2LogoutHook;
import com.wzkris.auth.domain.vo.LoginUserVO;
import com.wzkris.auth.oauth2.redis.JdkRedisUtil;
import com.wzkris.auth.service.CaptchaService;
import com.wzkris.common.core.domain.Result;
import com.wzkris.common.redis.util.RedisUtil;
import com.wzkris.common.security.oauth2.constants.OAuth2Type;
import com.wzkris.common.security.oauth2.domain.OAuth2User;
import com.wzkris.common.security.oauth2.domain.model.LoginSyser;
import com.wzkris.common.security.utils.SysUtil;
import com.wzkris.user.api.RemoteSysUserApi;
import com.wzkris.user.api.domain.vo.RouterVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.redisson.api.RBucket;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.List;

import static com.wzkris.common.core.domain.Result.success;

@Tag(name = "管理员登录态")
@RestController
@RequestMapping("/login_user")
@RequiredArgsConstructor
public class LoginUserController {

    private static final String ROUTER_PREFIX = "router";
    private final TokenConfig tokenConfig;
    private final RemoteSysUserApi remoteSysUserApi;
    private final CaptchaService captchaService;

    // 构建用户路由KEY
    private static String buildRouterKey(Long userId) {
        return String.format("%s:%s", ROUTER_PREFIX, userId);
    }

    @Operation(summary = "用户信息")
    @GetMapping
    public Result<LoginUserVO> loginUser() {
        LoginSyser loginSyser = SysUtil.getLoginSyser();
        LoginUserVO loginUserVO = new LoginUserVO();
        loginUserVO.setUsername(loginSyser.getUsername());
        loginUserVO.setIsAdmin(loginSyser.getIsAdmin());
        loginUserVO.setAuthorities(SysUtil.getAuthorities());
        return success(loginUserVO);
    }

    @Operation(summary = "路由树")
    @GetMapping("/routing")
    public Result<List<RouterVO>> routers() {
        // 获取前端路由，缓存路由结果
        Long userId = SysUtil.getUserId();
        RBucket<List<RouterVO>> bucket = JdkRedisUtil.getRedissonClient().getBucket(buildRouterKey(userId));
        if (bucket.get() == null) {
            Result<List<RouterVO>> listResult = remoteSysUserApi.getRouter(userId);
            List<RouterVO> routerVOS = listResult.checkData();
            bucket.set(routerVOS, Duration.ofSeconds(tokenConfig.getAccessTokenTimeOut()));
        }
        return success(bucket.get());
    }

    @Operation(summary = "登录异常账户解锁")
    @PostMapping("/unlock")
    @PreAuthorize("@ps.hasPerms('account:unlock')")
    public Result<?> unlock(@RequestBody String username) {
        captchaService.unlock(username);
        return success();
    }

    /**
     * 回调钩子实现
     */
    @Component
    public static class LoginUserLogoutHook extends OAuth2LogoutHook {
        @Override
        public void doLogoutHook(@NotNull OAuth2User oAuth2User) {
            RedisUtil.delObj(buildRouterKey(((LoginSyser) oAuth2User.getDetails()).getUserId()));
        }

        @Override
        public boolean check(@NotNull OAuth2User oAuth2User) {
            return OAuth2Type.SYS_USER.getValue().equals(oAuth2User.getOauth2Type());
        }
    }
}
