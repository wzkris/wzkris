package com.wzkris.user.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.core.domain.Result;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateType;
import com.wzkris.common.web.model.BaseController;
import com.wzkris.user.domain.SysMenu;
import com.wzkris.user.mapper.SysMenuMapper;
import com.wzkris.user.service.SysMenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜单信息
 *
 * @author wzkris
 */
@Tag(name = "菜单管理")
@RestController
@RequestMapping("/sys_menu")
@PreAuthorize("@SysUtil.isSuperTenant()")// 只允许超级租户访问
@RequiredArgsConstructor
public class SysMenuController extends BaseController {
    private final SysMenuMapper menuMapper;
    private final SysMenuService menuService;

    @Operation(summary = "菜单列表")
    @GetMapping("/list")
    @PreAuthorize("@ps.hasPerms('menu:list')")
    public Result<List<SysMenu>> list(SysMenu menu) {
        List<SysMenu> menus = menuMapper.selectList(this.buildQueryWrapper(menu));
        return ok(menus);
    }

    private LambdaQueryWrapper<SysMenu> buildQueryWrapper(SysMenu menu) {
        return new LambdaQueryWrapper<SysMenu>()
                .eq(StringUtil.isNotNull(menu.getMenuId()), SysMenu::getMenuId, menu.getMenuId())
                .like(StringUtil.isNotNull(menu.getMenuName()), SysMenu::getMenuName, menu.getMenuName())
                .eq(StringUtil.isNotNull(menu.getMenuType()), SysMenu::getMenuType, menu.getMenuType())
                .eq(StringUtil.isNotNull(menu.getIsVisible()), SysMenu::getIsVisible, menu.getIsVisible())
                .eq(StringUtil.isNotNull(menu.getIsCache()), SysMenu::getIsCache, menu.getIsCache())
                .eq(StringUtil.isNotNull(menu.getIsFrame()), SysMenu::getIsFrame, menu.getIsFrame())
                .eq(StringUtil.isNotNull(menu.getStatus()), SysMenu::getStatus, menu.getStatus())
                .orderByDesc(SysMenu::getMenuSort, SysMenu::getMenuId);
    }

    @Operation(summary = "菜单详细信息")
    @GetMapping("/{menuId}")
    @PreAuthorize("@ps.hasPerms('menu:query')")
    public Result<SysMenu> getInfo(@PathVariable Long menuId) {
        return ok(menuMapper.selectById(menuId));
    }

    @Operation(summary = "新增菜单")
    @OperateLog(title = "菜单管理", subTitle = "新增菜单", operateType = OperateType.INSERT)
    @PostMapping("/add")
    @PreAuthorize("@ps.hasPerms('menu:add')")
    public Result<Void> add(@Valid @RequestBody SysMenu menu) {
        if (menu.getIsFrame() && !StringUtil.ishttp(menu.getPath())) {
            return fail("新增菜单'" + menu.getMenuName() + "'失败，地址必须以http(s)://开头");
        }
        return toRes(menuMapper.insert(menu));
    }

    @Operation(summary = "修改菜单")
    @OperateLog(title = "菜单管理", subTitle = "修改菜单", operateType = OperateType.UPDATE)
    @PostMapping("/edit")
    @PreAuthorize("@ps.hasPerms('menu:edit')")
    public Result<Void> edit(@Valid @RequestBody SysMenu menu) {
        if (menu.getIsFrame() && !StringUtil.ishttp(menu.getPath())) {
            return fail("修改菜单'" + menu.getMenuName() + "'失败，地址必须以http(s)://开头");
        }
        else if (menu.getMenuId().equals(menu.getParentId())) {
            return fail("修改菜单'" + menu.getMenuName() + "'失败，上级菜单不能选择自己");
        }
        return toRes(menuMapper.updateById(menu));
    }

    @Operation(summary = "删除菜单")
    @OperateLog(title = "菜单管理", subTitle = "删除菜单", operateType = OperateType.DELETE)
    @PostMapping("/remove")
    @PreAuthorize("@ps.hasPerms('menu:remove')")
    public Result<Void> remove(@RequestBody Long menuId) {
        if (menuService.hasChildByMenuId(menuId)) {
            return fail("存在子菜单,不允许删除");
        }
        if (menuService.checkMenuExistRole(menuId)) {
            return fail("菜单已分配,不允许删除");
        }
        menuService.deleteById(menuId);
        return ok();
    }
}
