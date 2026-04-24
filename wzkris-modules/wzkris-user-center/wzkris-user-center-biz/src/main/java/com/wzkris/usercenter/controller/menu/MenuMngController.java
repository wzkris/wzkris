package com.wzkris.usercenter.controller.menu;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateTypeEnum;
import com.wzkris.common.security.annotation.CheckAdminPerms;
import com.wzkris.usercenter.api.menu.MenuMngApi;
import com.wzkris.usercenter.request.common.IdRequest;
import com.wzkris.usercenter.request.menu.MenuMngListRequest;
import com.wzkris.usercenter.request.menu.MenuMngSaveRequest;
import com.wzkris.usercenter.request.menu.MenuMngUpdateRequest;
import com.wzkris.usercenter.response.menu.MenuInfoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "菜单管理")
@RestController
@RequestMapping("/menu-manage")
@RequiredArgsConstructor
public class MenuMngController {

    private final MenuMngApi menuMngApi;

    @Operation(summary = "菜单列表（无分页）")
    @GetMapping("/query-list")
    @CheckAdminPerms("user-mod:menu-mng:list")
    public Result<List<MenuInfoResponse>> queryList(MenuMngListRequest request) {
        return menuMngApi.queryList(request);
    }

    @Operation(summary = "菜单详细信息")
    @GetMapping("/query-info/{id}")
    @CheckAdminPerms("user-mod:menu-mng:list")
    public Result<MenuInfoResponse> queryInfo(IdRequest request) {
        return menuMngApi.queryInfo(request);
    }

    @Operation(summary = "新增菜单")
    @OperateLog(title = "菜单管理", subTitle = "新增菜单", type = OperateTypeEnum.INSERT)
    @PostMapping("/save")
    @CheckAdminPerms("user-mod:menu-mng:add")
    public Result<Void> save(@Validated @RequestBody MenuMngSaveRequest request) {
        return menuMngApi.save(request);
    }

    @Operation(summary = "修改菜单")
    @OperateLog(title = "菜单管理", subTitle = "修改菜单", type = OperateTypeEnum.UPDATE)
    @PostMapping("/update")
    @CheckAdminPerms("user-mod:menu-mng:edit")
    public Result<Void> update(@Validated @RequestBody MenuMngUpdateRequest request) {
        return menuMngApi.update(request);
    }

    @Operation(summary = "删除菜单")
    @OperateLog(title = "菜单管理", subTitle = "删除菜单", type = OperateTypeEnum.DELETE)
    @PostMapping("/remove")
    @CheckAdminPerms("user-mod:menu-mng:remove")
    public Result<Void> remove(@RequestBody @Valid IdRequest request) {
        return menuMngApi.remove(request);
    }

}
