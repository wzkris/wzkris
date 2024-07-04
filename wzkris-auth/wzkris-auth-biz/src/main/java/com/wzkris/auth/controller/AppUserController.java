package com.wzkris.auth.controller;

import com.wzkris.auth.model.AppUserVO;
import com.wzkris.common.core.domain.Result;
import com.wzkris.common.core.utils.MapstructUtil;
import com.wzkris.common.security.oauth2.domain.model.LoginApper;
import com.wzkris.common.security.utils.AppUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.wzkris.common.core.domain.Result.success;

@Tag(name = "用户登录态")
@RestController
@RequestMapping("/app_user")
@RequiredArgsConstructor
public class AppUserController {

    @Operation(summary = "用户信息")
    @GetMapping
    public Result<AppUserVO> appUser() {
        LoginApper loginApper = AppUtil.getAppUser();
        AppUserVO appUserVO = MapstructUtil.convert(loginApper, AppUserVO.class);
        return success(appUserVO);
    }

}
