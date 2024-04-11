package com.thingslink.auth.controller;


import com.thingslink.auth.domain.vo.RouterVO;
import com.thingslink.auth.service.SysMenuService;
import com.thingslink.common.core.domain.Result;
import com.thingslink.common.security.utils.LoginUserUtil;
import com.thingslink.common.web.controller.BaseController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "登录态信息")
@RestController
@RequestMapping
@RequiredArgsConstructor
public class UserInfoController extends BaseController {

    private final SysMenuService menuService;

    @Operation(summary = "用户信息")
    @GetMapping("user_info")
    public Result<?> userinfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return success(authentication.getPrincipal());
    }

    @Operation(summary = "前端路由")
    @GetMapping("routing")
    public Result<List<RouterVO>> routers() {
        List<RouterVO> routerVOS = menuService.listRouteTree(LoginUserUtil.getUserId());
        return success(routerVOS);
    }
}
