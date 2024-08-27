package com.wzkris.auth.controller;


import com.wzkris.auth.domain.vo.LoginUserVO;
import com.wzkris.auth.service.CaptchaService;
import com.wzkris.common.core.domain.Result;
import com.wzkris.common.security.oauth2.domain.model.LoginSyser;
import com.wzkris.common.security.utils.SysUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.wzkris.common.core.domain.Result.success;

@Tag(name = "管理员登录态")
@RestController
@RequestMapping("/login_user")
@RequiredArgsConstructor
public class LoginUserController {

    private final CaptchaService captchaService;

    @Operation(summary = "用户信息")
    @GetMapping
    public Result<LoginUserVO> loginUser() {
        LoginSyser loginSyser = SysUtil.getLoginSyser();
        LoginUserVO loginUserVO = new LoginUserVO();
        loginUserVO.setUsername(loginSyser.getUsername());
        loginUserVO.setAdministrator(loginSyser.isAdministrator());
        loginUserVO.setAuthorities(SysUtil.getAuthorities());
        return success(loginUserVO);
    }

    @Operation(summary = "登录异常账户解锁")
    @PostMapping("/unlock")
    @PreAuthorize("@ps.hasPerms('account:unlock')")
    public Result<?> unlock(@RequestBody String username) {
        captchaService.unlock(username);
        return success();
    }

}
