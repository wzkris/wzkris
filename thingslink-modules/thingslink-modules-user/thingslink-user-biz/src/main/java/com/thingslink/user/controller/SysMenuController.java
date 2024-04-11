package com.thingslink.user.controller;

import com.thingslink.user.domain.SysMenu;
import com.thingslink.user.domain.vo.SelectTree;
import com.thingslink.user.mapper.SysMenuMapper;
import com.thingslink.user.mapper.SysRoleMenuMapper;
import com.thingslink.user.mapper.SysTenantPackageMapper;
import com.thingslink.user.service.SysMenuService;
import com.thingslink.common.core.constant.CommonConstants;
import com.thingslink.common.core.domain.Result;
import com.thingslink.common.core.utils.StringUtil;
import com.thingslink.common.log.annotation.OperateLog;
import com.thingslink.common.log.enums.OperateType;
import com.thingslink.common.orm.model.BaseController;
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
@RequestMapping("/menu")
@RequiredArgsConstructor
public class SysMenuController extends BaseController {
    private final SysRoleMenuMapper sysRoleMenuMapper;
    private final SysMenuMapper sysMenuMapper;
    private final SysTenantPackageMapper sysTenantPackageMapper;
    private final SysMenuService sysMenuService;

    @Operation(summary = "菜单列表")
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('menu:list') && @LoginUserUtil.isSuperTenant()")
    public Result<?> list(SysMenu menu) {
        List<SysMenu> menus = sysMenuService.list(menu);
        return success(menus);
    }

    @Operation(summary = "菜单详细信息")
    @GetMapping("/{menuId}")
    @PreAuthorize("hasAuthority('menu:query') && @LoginUserUtil.isSuperTenant()")
    public Result<?> getInfo(@PathVariable Long menuId) {
        return success(sysMenuMapper.selectById(menuId));
    }

    @Operation(summary = "菜单下拉树列表")
    @GetMapping("/treeList")
    public Result<?> treeList(SysMenu menu) {
        List<SelectTree> selectTrees = sysMenuService.listMenuSelectTree(menu);
        return success(selectTrees);
    }

    @Operation(summary = "角色菜单列表树")
    @GetMapping("/roleMenuTreeList/{roleId}")
    @PreAuthorize("hasAuthority('menu:list')")
    public Result<?> roleMenuTreeList(@PathVariable Long roleId) {
        Map<String, Object> res = new HashMap<>(2);
        res.put("checkedKeys", sysRoleMenuMapper.listMenuIdByRoleIds(roleId));
        res.put("menus", sysMenuService.listMenuSelectTree(new SysMenu()));
        return success(res);
    }

    @Operation(summary = "租户套餐菜单列表树")
    @GetMapping("/tenantPackageMenuTreeList/{packageId}")
    @PreAuthorize("hasAuthority('menu:list') && @LoginUserUtil.isSuperTenant()")
    public Result<?> tenantPackageMenuTreeList(@PathVariable Long packageId) {
        Map<String, Object> res = new HashMap<>(2);
        res.put("checkedKeys", sysTenantPackageMapper.listMenuIdByPackageId(packageId));
        res.put("menus", sysMenuService.listMenuSelectTree(new SysMenu()));
        return success(res);
    }

    @Operation(summary = "新增菜单")
    @OperateLog(title = "菜单管理", operateType = OperateType.INSERT)
    @PostMapping
    @PreAuthorize("hasAuthority('menu:add') && @LoginUserUtil.isSuperTenant()")
    public Result<?> add(@Valid @RequestBody SysMenu menu) {
        if (StringUtil.equals(CommonConstants.NOT_UNIQUE, sysMenuService.checkMenuNameUnique(menu.getMenuName(), menu.getParentId()))) {
            return fail("新增菜单'" + menu.getMenuName() + "'失败，菜单名称已存在");
        }
        else if (menu.getIsFrame() && !StringUtil.ishttp(menu.getPath())) {
            return fail("新增菜单'" + menu.getMenuName() + "'失败，地址必须以http(s)://开头");
        }
        return toRes(sysMenuMapper.insert(menu));
    }

    @Operation(summary = "修改菜单")
    @OperateLog(title = "菜单管理", operateType = OperateType.UPDATE)
    @PutMapping
    @PreAuthorize("hasAuthority('menu:edit') && @LoginUserUtil.isSuperTenant()")
    public Result<?> edit(@Valid @RequestBody SysMenu menu) {
        if (StringUtil.equals(CommonConstants.NOT_UNIQUE, sysMenuService.checkMenuNameUnique(menu.getMenuName(), menu.getParentId()))) {
            return fail("修改菜单'" + menu.getMenuName() + "'失败，菜单名称已存在");
        }
        else if (menu.getIsFrame() && !StringUtil.ishttp(menu.getPath())) {
            return fail("修改菜单'" + menu.getMenuName() + "'失败，地址必须以http(s)://开头");
        }
        else if (menu.getMenuId().equals(menu.getParentId())) {
            return fail("修改菜单'" + menu.getMenuName() + "'失败，上级菜单不能选择自己");
        }
        return toRes(sysMenuMapper.updateById(menu));
    }

    @Operation(summary = "删除菜单")
    @OperateLog(title = "菜单管理", operateType = OperateType.DELETE)
    @DeleteMapping("/{menuId}")
    @PreAuthorize("hasAuthority('menu:remove') && @LoginUserUtil.isSuperTenant()")
    public Result<?> remove(@PathVariable Long menuId) {
        if (sysMenuService.hasChildByMenuId(menuId)) {
            return fail("存在子菜单,不允许删除");
        }
        if (sysMenuService.checkMenuExistRole(menuId)) {
            return fail("菜单已分配,不允许删除");
        }
        return toRes(sysMenuMapper.deleteById(menuId));
    }
}
