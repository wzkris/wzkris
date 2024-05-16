package com.thingslink.auth.controller;


import com.thingslink.auth.domain.vo.LoginUserVO;
import com.thingslink.auth.oauth2.service.SysUserDetailsService;
import com.thingslink.common.core.domain.Result;
import com.thingslink.common.security.oauth2.model.LoginSysUser;
import com.thingslink.common.security.utils.SysUtil;
import com.thingslink.user.api.domain.vo.RouterVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.thingslink.common.core.domain.Result.success;

@Tag(name = "管理员登录态")
@RestController
@RequestMapping("/login_user")
@RequiredArgsConstructor
public class LoginUserController {

    private final SysUserDetailsService sysUserDetailsService;

    @Operation(summary = "用户信息")
    @GetMapping
    public Result<LoginUserVO> loginUser() {
        LoginSysUser loginUser = SysUtil.getLoginUser();
        LoginUserVO loginUserVO = new LoginUserVO();
        loginUserVO.setUsername(loginUser.getUsername());
        loginUserVO.setIsAdmin(loginUser.getIsAdmin());
        loginUserVO.setAuthorities(SysUtil.getAuthorities());
        return success(loginUserVO);
    }

    @Operation(summary = "路由树")
    @GetMapping("routing")
    public Result<List<RouterVO>> routers() {
        List<RouterVO> router = sysUserDetailsService.getRouter(SysUtil.getUserId());
        return success(router);
    }
}
