package com.wzkris.user.controller.menu;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.core.domain.Result;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateType;
import com.wzkris.common.orm.model.BaseController;
import com.wzkris.common.security.annotation.CheckUserPerms;
import com.wzkris.common.security.utils.LoginUserUtil;
import com.wzkris.common.web.utils.BeanUtil;
import com.wzkris.user.constant.MenuConstants;
import com.wzkris.user.domain.MenuInfoDO;
import com.wzkris.user.domain.req.menu.MenuManageQueryReq;
import com.wzkris.user.domain.req.menu.MenuManageReq;
import com.wzkris.user.mapper.MenuInfoMapper;
import com.wzkris.user.service.MenuInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 菜单管理
 *
 * @author wzkris
 */
@Tag(name = "菜单管理")
@RestController
@RequestMapping("/menu-manage")
@PreAuthorize("@lu.isSuperTenant()") // 只允许超级租户访问
@RequiredArgsConstructor
public class MenuManageController extends BaseController {

    private final MenuInfoMapper menuInfoMapper;

    private final MenuInfoService menuInfoService;

    @Operation(summary = "菜单列表")
    @GetMapping("/list")
    @CheckUserPerms("user-mod:menu-mng:list")
    public Result<List<MenuInfoDO>> list(MenuManageQueryReq queryReq) {
        List<MenuInfoDO> menus = menuInfoMapper.selectList(this.buildQueryWrapper(queryReq));
        return ok(menus);
    }

    private LambdaQueryWrapper<MenuInfoDO> buildQueryWrapper(MenuManageQueryReq queryReq) {
        List<Long> menuIds = new ArrayList<>();
        if (!LoginUserUtil.isAdmin()) {
            menuIds = menuInfoService.listMenuIdByUserId(LoginUserUtil.getId());
        }
        return new LambdaQueryWrapper<MenuInfoDO>()
                .in(CollectionUtils.isNotEmpty(menuIds), MenuInfoDO::getMenuId, menuIds)
                .like(StringUtil.isNotEmpty(queryReq.getMenuName()), MenuInfoDO::getMenuName, queryReq.getMenuName())
                .eq(StringUtil.isNotEmpty(queryReq.getStatus()), MenuInfoDO::getStatus, queryReq.getStatus())
                .orderByDesc(MenuInfoDO::getMenuSort, MenuInfoDO::getMenuId);
    }

    @Operation(summary = "菜单详细信息")
    @GetMapping("/{menuId}")
    @CheckUserPerms("user-mod:menu-mng:query")
    public Result<MenuInfoDO> getInfo(@PathVariable Long menuId) {
        return ok(menuInfoMapper.selectById(menuId));
    }

    @Operation(summary = "新增菜单")
    @OperateLog(title = "菜单管理", subTitle = "新增菜单", operateType = OperateType.INSERT)
    @PostMapping("/add")
    @CheckUserPerms("user-mod:menu-mng:add")
    public Result<Void> add(@Validated @RequestBody MenuManageReq req) {
        if (StringUtil.equalsAny(req.getMenuType(), MenuConstants.TYPE_INNERLINK, MenuConstants.TYPE_OUTLINK)
                && !StringUtil.ishttp(req.getPath())) {
            return err40000("新增菜单'" + req.getMenuName() + "'失败，地址必须以http(s)://开头");
        }
        return toRes(menuInfoMapper.insert(BeanUtil.convert(req, MenuInfoDO.class)));
    }

    @Operation(summary = "修改菜单")
    @OperateLog(title = "菜单管理", subTitle = "修改菜单", operateType = OperateType.UPDATE)
    @PostMapping("/edit")
    @CheckUserPerms("user-mod:menu-mng:edit")
    public Result<Void> edit(@Validated @RequestBody MenuManageReq req) {
        if (StringUtil.equalsAny(req.getMenuType(), MenuConstants.TYPE_INNERLINK, MenuConstants.TYPE_OUTLINK)
                && !StringUtil.ishttp(req.getPath())) {
            return err40000("修改菜单'" + req.getMenuName() + "'失败，地址必须以http(s)://开头");
        } else if (req.getMenuId().equals(req.getParentId())) {
            return err40000("修改菜单'" + req.getMenuName() + "'失败，上级菜单不能选择自己");
        }
        return toRes(menuInfoMapper.updateById(BeanUtil.convert(req, MenuInfoDO.class)));
    }

    @Operation(summary = "删除菜单")
    @OperateLog(title = "菜单管理", subTitle = "删除菜单", operateType = OperateType.DELETE)
    @PostMapping("/remove")
    @CheckUserPerms("user-mod:menu-mng:remove")
    public Result<Void> remove(@RequestBody Long menuId) {
        if (menuInfoService.existSubMenu(menuId)) {
            return err40000("存在子菜单,不允许删除");
        }
        if (menuInfoService.existRole(menuId)) {
            return err40000("菜单已分配,不允许删除");
        }
        return toRes(menuInfoService.removeById(menuId));
    }

}
