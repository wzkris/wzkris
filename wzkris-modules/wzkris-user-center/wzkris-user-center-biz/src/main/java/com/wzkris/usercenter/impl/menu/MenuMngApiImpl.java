package com.wzkris.usercenter.impl.menu;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.orm.model.AbstractApi;
import com.wzkris.common.security.utils.SecurityUtil;
import com.wzkris.common.web.utils.BeanUtil;
import com.wzkris.usercenter.api.menu.MenuMngApi;
import com.wzkris.usercenter.domain.MenuInfoDO;
import com.wzkris.usercenter.enums.MenuTypeEnum;
import com.wzkris.usercenter.request.StatusUpdateRequest;
import com.wzkris.usercenter.request.menu.MenuMngQueryRequest;
import com.wzkris.usercenter.request.menu.MenuMngSaveRequest;
import com.wzkris.usercenter.request.menu.MenuMngUpdateRequest;
import com.wzkris.usercenter.response.menu.MenuInfoResponse;
import com.wzkris.usercenter.service.MenuInfoService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuMngApiImpl extends AbstractApi implements MenuMngApi {

    private final MenuInfoService menuInfoService;

    @Override
    public Result<List<MenuInfoResponse>> queryList(MenuMngQueryRequest request) {
        List<MenuInfoDO> menus = menuInfoService.list(this.buildQueryWrapper(request));
        return ok(BeanUtil.convert(menus, MenuInfoResponse.class));
    }

    private LambdaQueryWrapper<MenuInfoDO> buildQueryWrapper(MenuMngQueryRequest request) {
        List<Long> menuIds = new ArrayList<>();
        if (!SecurityUtil.isSuper()) {
            menuIds = menuInfoService.listMenuIdByAdminId(SecurityUtil.getUid());
        }
        return new LambdaQueryWrapper<MenuInfoDO>()
                .in(CollectionUtils.isNotEmpty(menuIds), MenuInfoDO::getMenuId, menuIds)
                .like(StringUtil.isNotEmpty(request.getMenuName()), MenuInfoDO::getMenuName, request.getMenuName())
                .eq(StringUtil.isNotEmpty(request.getStatus()), MenuInfoDO::getStatus, request.getStatus())
                .eq(StringUtil.isNotEmpty(request.getScope()), MenuInfoDO::getScope, request.getScope())
                .orderByDesc(MenuInfoDO::getMenuSort, MenuInfoDO::getMenuId);
    }

    @Override
    public Result<MenuInfoResponse> queryInfo(Long menuId) {
        return ok(BeanUtil.convert(menuInfoService.getById(menuId), MenuInfoResponse.class));
    }

    @Override
    public Result<Void> save(MenuMngSaveRequest request) {
        if (StringUtil.equalsAny(request.getMenuType(), MenuTypeEnum.INNERLINK.getValue(), MenuTypeEnum.OUTLINK.getValue())
                && !StringUtil.ishttp(request.getPath())) {
            return requestFail("新增菜单'" + request.getMenuName() + "'失败，地址必须以http(s)://开头");
        }
        return toRes(menuInfoService.save(BeanUtil.convert(request, MenuInfoDO.class)));
    }

    @Override
    public Result<Void> update(MenuMngUpdateRequest request) {
        if (StringUtil.equalsAny(request.getMenuType(), MenuTypeEnum.INNERLINK.getValue(), MenuTypeEnum.OUTLINK.getValue())
                && !StringUtil.ishttp(request.getPath())) {
            return requestFail("修改菜单'" + request.getMenuName() + "'失败，地址必须以http(s)://开头");
        } else if (request.getMenuId().equals(request.getParentId())) {
            return requestFail("修改菜单'" + request.getMenuName() + "'失败，上级菜单不能选择自己");
        }
        return toRes(menuInfoService.updateById(BeanUtil.convert(request, MenuInfoDO.class)));
    }

    @Override
    public Result<Void> editStatus(StatusUpdateRequest request) {
        MenuInfoDO update = new MenuInfoDO(request.getId());
        update.setStatus(request.getStatus());
        return toRes(menuInfoService.updateById(update));
    }

    @Override
    public Result<Void> remove(Long menuId) {
        if (menuInfoService.existSubMenu(menuId)) {
            return requestFail("存在子菜单,不允许删除");
        }
        return toRes(menuInfoService.removeMenu(menuId));
    }

}
