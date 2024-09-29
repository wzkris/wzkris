package com.wzkris.auth.controller;

import com.wzkris.auth.domain.vo.AppUserVO;
import com.wzkris.auth.domain.vo.LoginUserVO;
import com.wzkris.auth.service.CaptchaService;
import com.wzkris.common.core.domain.Result;
import com.wzkris.common.core.utils.MapstructUtil;
import com.wzkris.common.security.oauth2.domain.model.LoginApper;
import com.wzkris.common.security.oauth2.domain.model.LoginSyser;
import com.wzkris.common.security.utils.AppUtil;
import com.wzkris.common.security.utils.SysUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.wzkris.common.core.domain.Result.success;

@Tag(name = "用户登录态")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final CaptchaService captchaService;

    @Operation(summary = "app用户信息")
    @GetMapping("/app_user")
    public Result<AppUserVO> appUser() {
        LoginApper loginApper = AppUtil.getAppUser();
        AppUserVO appUserVO = MapstructUtil.convert(loginApper, AppUserVO.class);
        return success(appUserVO);
    }

    @Operation(summary = "系统用户信息")
    @GetMapping("/login_user")
    public Result<LoginUserVO> loginUser() {
        LoginSyser loginSyser = SysUtil.getLoginSyser();
        LoginUserVO loginUserVO = new LoginUserVO();
        loginUserVO.setUsername(loginSyser.getUsername());
        loginUserVO.setAdministrator(loginSyser.isAdministrator());
        loginUserVO.setAuthorities(SysUtil.getAuthorities());
        return success(loginUserVO);
    }

    @Operation(summary = "系统用户登录异常账户解锁")
    @PostMapping("/login_user/unlock")
    @PreAuthorize("@ps.hasPerms('account:unlock')")
    public Result<?> unlock(@RequestBody String username) {
        captchaService.unlock(username);
        return success();
    }
}
