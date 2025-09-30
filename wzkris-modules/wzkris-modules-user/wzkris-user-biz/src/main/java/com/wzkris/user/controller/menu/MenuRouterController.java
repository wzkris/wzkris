package com.wzkris.user.controller.menu;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.BaseController;
import com.wzkris.common.security.utils.LoginUserUtil;
import com.wzkris.user.domain.vo.RouterVO;
import com.wzkris.user.service.MenuInfoService;
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
@Tag(name = "菜单路由")
@RestController
@RequestMapping("/menu")
@RequiredArgsConstructor
public class MenuRouterController extends BaseController {

    private final MenuInfoService menuInfoService;

    @Operation(summary = "获取路由")
    @GetMapping("/router")
    public Result<List<RouterVO>> routers() {
        List<RouterVO> routerVOS = menuInfoService.listRouter(LoginUserUtil.getId());
        return ok(routerVOS);
    }

}
