package com.wzkris.user.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.core.domain.Result;
import com.wzkris.common.core.utils.BeanUtil;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateType;
import com.wzkris.common.security.oauth2.annotation.CheckSystemPerms;
import com.wzkris.common.security.utils.LoginUtil;
import com.wzkris.common.web.model.BaseController;
import com.wzkris.user.constant.MenuConstants;
import com.wzkris.user.domain.SysMenu;
import com.wzkris.user.domain.req.SysMenuQueryReq;
import com.wzkris.user.domain.req.SysMenuReq;
import com.wzkris.user.mapper.SysMenuMapper;
import com.wzkris.user.service.SysMenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 菜单管理
 *
 * @author wzkris
 */
@Tag(name = "菜单管理")
@RestController
@RequestMapping("/sys_menu")
@PreAuthorize("@lg.isSuperTenant()") // 只允许超级租户访问
@RequiredArgsConstructor
public class SysMenuController extends BaseController {

    private final SysMenuMapper menuMapper;

    private final SysMenuService menuService;

    @Operation(summary = "菜单列表")
    @GetMapping("/list")
    @CheckSystemPerms("sys_menu:list")
    public Result<List<SysMenu>> list(SysMenuQueryReq queryReq) {
        List<SysMenu> menus = menuMapper.selectList(this.buildQueryWrapper(queryReq));
        return ok(menus);
    }

    private LambdaQueryWrapper<SysMenu> buildQueryWrapper(SysMenuQueryReq queryReq) {
        List<Long> menuIds = new ArrayList<>();
        if (!LoginUtil.isAdmin()) {
            menuIds = menuService.listMenuIdByUserId(LoginUtil.getUserId());
        }
        return new LambdaQueryWrapper<SysMenu>()
                .in(StringUtil.isNotEmpty(menuIds), SysMenu::getMenuId, menuIds)
                .like(StringUtil.isNotNull(queryReq.getMenuName()), SysMenu::getMenuName, queryReq.getMenuName())
                .eq(StringUtil.isNotNull(queryReq.getStatus()), SysMenu::getStatus, queryReq.getStatus())
                .orderByDesc(SysMenu::getMenuSort, SysMenu::getMenuId);
    }

    @Operation(summary = "菜单详细信息")
    @GetMapping("/{menuId}")
    @CheckSystemPerms("sys_menu:query")
    public Result<SysMenu> getInfo(@PathVariable Long menuId) {
        return ok(menuMapper.selectById(menuId));
    }

    @Operation(summary = "新增菜单")
    @OperateLog(title = "菜单管理", subTitle = "新增菜单", operateType = OperateType.INSERT)
    @PostMapping("/add")
    @CheckSystemPerms("sys_menu:add")
    public Result<Void> add(@Validated @RequestBody SysMenuReq req) {
        if (StringUtil.equalsAny(req.getMenuType(), MenuConstants.TYPE_INNERLINK, MenuConstants.TYPE_OUTLINK)
                && !StringUtil.ishttp(req.getPath())) {
            return err412("新增菜单'" + req.getMenuName() + "'失败，地址必须以http(s)://开头");
        }
        return toRes(menuMapper.insert(BeanUtil.convert(req, SysMenu.class)));
    }

    @Operation(summary = "修改菜单")
    @OperateLog(title = "菜单管理", subTitle = "修改菜单", operateType = OperateType.UPDATE)
    @PostMapping("/edit")
    @CheckSystemPerms("sys_menu:edit")
    public Result<Void> edit(@Validated @RequestBody SysMenuReq req) {
        if (StringUtil.equalsAny(req.getMenuType(), MenuConstants.TYPE_INNERLINK, MenuConstants.TYPE_OUTLINK)
                && !StringUtil.ishttp(req.getPath())) {
            return err412("修改菜单'" + req.getMenuName() + "'失败，地址必须以http(s)://开头");
        } else if (req.getMenuId().equals(req.getParentId())) {
            return err412("修改菜单'" + req.getMenuName() + "'失败，上级菜单不能选择自己");
        }
        return toRes(menuMapper.updateById(BeanUtil.convert(req, SysMenu.class)));
    }

    @Operation(summary = "删除菜单")
    @OperateLog(title = "菜单管理", subTitle = "删除菜单", operateType = OperateType.DELETE)
    @PostMapping("/remove")
    @CheckSystemPerms("sys_menu:remove")
    public Result<Void> remove(@RequestBody Long menuId) {
        if (menuService.checkMenuExistChild(menuId)) {
            return err412("存在子菜单,不允许删除");
        }
        if (menuService.checkMenuExistRole(menuId)) {
            return err412("菜单已分配,不允许删除");
        }
        return toRes(menuService.deleteById(menuId));
    }
}
