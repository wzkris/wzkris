package com.wzkris.user.controller;

import com.wzkris.common.core.domain.Result;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateType;
import com.wzkris.common.orm.model.BaseController;
import com.wzkris.user.domain.SysMenu;
import com.wzkris.user.domain.vo.SelectTree;
import com.wzkris.user.mapper.SysMenuMapper;
import com.wzkris.user.mapper.SysRoleMenuMapper;
import com.wzkris.user.mapper.SysTenantPackageMapper;
import com.wzkris.user.service.SysMenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 菜单信息
 *
 * @author wzkris
 */
@Tag(name = "菜单管理")
@RestController
@RequestMapping("/sys_menu")
@RequiredArgsConstructor
public class SysMenuController extends BaseController {
    private final SysRoleMenuMapper sysRoleMenuMapper;
    private final SysMenuMapper sysMenuMapper;
    private final SysTenantPackageMapper sysTenantPackageMapper;
    private final SysMenuService sysMenuService;

    @Operation(summary = "菜单列表")
    @GetMapping("/list")
    @PreAuthorize("@ps.hasPerms('menu:list') && @SysUtil.isSuperTenant()")
    public Result<?> list(SysMenu menu) {
        List<SysMenu> menus = sysMenuService.list(menu);
        return success(menus);
    }

    @Operation(summary = "菜单详细信息")
    @GetMapping("/{menuId}")
    @PreAuthorize("@ps.hasPerms('menu:query') && @SysUtil.isSuperTenant()")
    public Result<?> getInfo(@PathVariable Long menuId) {
        return success(sysMenuMapper.selectById(menuId));
    }

    @Operation(summary = "菜单下拉选择树")
    @GetMapping("/treelist")
    @PreAuthorize("@ps.hasPerms('menu:selecttree')")
    public Result<?> menuTree(SysMenu menu) {
        List<SelectTree> selectTrees = sysMenuService.listMenuSelectTree(menu);
        return success(selectTrees);
    }

    @Operation(summary = "角色菜单列表树")
    @GetMapping("/tree_by_role/{roleId}")
    @PreAuthorize("@ps.hasPerms('menu:selecttree')")
    public Result<?> roleMenuTreeList(@PathVariable Long roleId) {
        Map<String, Object> res = new HashMap<>(2);
        res.put("checkedKeys", sysRoleMenuMapper.listMenuIdByRoleIds(roleId));
        res.put("menus", sysMenuService.listMenuSelectTree(new SysMenu()));
        return success(res);
    }

    @Operation(summary = "租户套餐菜单列表树")
    @GetMapping("/tree_by_tenant_package/{packageId}")
    @PreAuthorize("@ps.hasPerms('menu:list') && @SysUtil.isSuperTenant()")
    public Result<?> tenantPackageMenuTreeList(@PathVariable Long packageId) {
        Map<String, Object> res = new HashMap<>(2);
        res.put("checkedKeys", sysTenantPackageMapper.listMenuIdByPackageId(packageId));
        SysMenu sysMenu = new SysMenu();
        sysMenu.setIsTenant(true);
        res.put("menus", sysMenuService.listMenuSelectTree(sysMenu));// 查询租户专用菜单
        return success(res);
    }

    @Operation(summary = "新增菜单")
    @OperateLog(title = "菜单管理", operateType = OperateType.INSERT)
    @PostMapping("/add")
    @PreAuthorize("@ps.hasPerms('menu:add') && @SysUtil.isSuperTenant()")
    public Result<?> add(@Valid @RequestBody SysMenu menu) {
        if (menu.getIsFrame() && !StringUtil.ishttp(menu.getPath())) {
            return fail("新增菜单'" + menu.getMenuName() + "'失败，地址必须以http(s)://开头");
        }
        return toRes(sysMenuMapper.insert(menu));
    }

    @Operation(summary = "修改菜单")
    @OperateLog(title = "菜单管理", operateType = OperateType.UPDATE)
    @PostMapping("/edit")
    @PreAuthorize("@ps.hasPerms('menu:edit') && @SysUtil.isSuperTenant()")
    public Result<?> edit(@Valid @RequestBody SysMenu menu) {
        if (menu.getIsFrame() && !StringUtil.ishttp(menu.getPath())) {
            return fail("修改菜单'" + menu.getMenuName() + "'失败，地址必须以http(s)://开头");
        }
        else if (menu.getMenuId().equals(menu.getParentId())) {
            return fail("修改菜单'" + menu.getMenuName() + "'失败，上级菜单不能选择自己");
        }
        return toRes(sysMenuMapper.updateById(menu));
    }

    @Operation(summary = "删除菜单")
    @OperateLog(title = "菜单管理", operateType = OperateType.DELETE)
    @PostMapping("/remove")
    @PreAuthorize("@ps.hasPerms('menu:remove') && @SysUtil.isSuperTenant()")
    public Result<?> remove(@RequestBody Long menuId) {
        if (sysMenuService.hasChildByMenuId(menuId)) {
            return fail("存在子菜单,不允许删除");
        }
        if (sysMenuService.checkMenuExistRole(menuId)) {
            return fail("菜单已分配,不允许删除");
        }
        return toRes(sysMenuMapper.deleteById(menuId));
    }
}
