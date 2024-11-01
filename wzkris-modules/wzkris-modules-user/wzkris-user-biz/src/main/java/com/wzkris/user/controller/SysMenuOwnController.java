package com.wzkris.user.controller;

import com.wzkris.common.core.domain.Result;
import com.wzkris.common.orm.model.BaseController;
import com.wzkris.common.security.utils.SysUtil;
import com.wzkris.user.domain.vo.RouterVO;
import com.wzkris.user.service.SysMenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 菜单信息
 *
 * @author wzkris
 */
@Tag(name = "路由信息")
@RestController
@RequestMapping("/menu")
@RequiredArgsConstructor
public class SysMenuOwnController extends BaseController {

    private final SysMenuService sysMenuService;

    @Operation(summary = "路由")
    @GetMapping("/router")
    public Result<List<RouterVO>> routers() {
        List<RouterVO> routerVOS = sysMenuService.listRouteTree(SysUtil.getUserId());
        return success(routerVOS);
    }
}
