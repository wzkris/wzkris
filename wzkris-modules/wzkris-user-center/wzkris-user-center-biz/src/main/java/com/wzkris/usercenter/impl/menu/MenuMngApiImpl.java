package com.wzkris.usercenter.impl.menu;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.BeanCopierUtil;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.orm.request.IdRequest;
import com.wzkris.common.security.utils.SecurityUtil;
import com.wzkris.common.web.model.AbstractApi;
import com.wzkris.usercenter.api.menu.MenuMngApi;
import com.wzkris.usercenter.api.menu.request.MenuMngSaveRequest;
import com.wzkris.usercenter.api.menu.request.MenuMngTreeRequest;
import com.wzkris.usercenter.api.menu.request.MenuMngUpdateRequest;
import com.wzkris.usercenter.api.menu.response.MenuMngQueryResponse;
import com.wzkris.usercenter.api.menu.response.MenuMngListResponse;
import com.wzkris.usercenter.domain.MenuInfoDO;
import com.wzkris.usercenter.enums.menu.MenuTypeEnum;
import com.wzkris.usercenter.service.MenuInfoService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MenuMngApiImpl extends AbstractApi implements MenuMngApi {

    private final MenuInfoService menuInfoService;

    @Override
    public Result<List<MenuMngListResponse>> queryList(MenuMngTreeRequest request) {
        List<MenuInfoDO> menus = menuInfoService.list(this.buildQueryWrapper(request));
        return ok(BeanCopierUtil.copyList(menus, MenuMngListResponse.class));
    }

    private LambdaQueryWrapper<MenuInfoDO> buildQueryWrapper(MenuMngTreeRequest request) {
        List<Long> menuIds = new ArrayList<>();
        if (!SecurityUtil.isSuperUser()) {
            menuIds = menuInfoService.listMenuIdByAdminId(SecurityUtil.getUid());
        }
        return new LambdaQueryWrapper<MenuInfoDO>()
                .in(CollectionUtils.isNotEmpty(menuIds), MenuInfoDO::getId, menuIds)
                .like(StringUtil.isNotEmpty(request.getMenuName()), MenuInfoDO::getMenuName, request.getMenuName())
                .eq(request.getStatus() != null, MenuInfoDO::getStatus, request.getStatus())
                .eq(Objects.nonNull(request.getScope()), MenuInfoDO::getScope, request.getScope())
                .orderByDesc(MenuInfoDO::getMenuSort, MenuInfoDO::getId);
    }

    @Override
    public Result<MenuMngQueryResponse> queryById(IdRequest request) {
        return ok(BeanCopierUtil.copy(menuInfoService.getById(request.getId()), MenuMngQueryResponse.class));
    }

    @Override
    public Result<Void> save(MenuMngSaveRequest request) {
        if ((Objects.equals(request.getMenuType(), MenuTypeEnum.INNERLINK)
                || Objects.equals(request.getMenuType(), MenuTypeEnum.OUTLINK))
                && !StringUtil.ishttp(request.getPath())) {
            return requestFail("新增菜单'" + request.getMenuName() + "'失败，地址必须以http(s)://开头");
        }
        MenuInfoDO menuInfoDO = BeanCopierUtil.copy(request, MenuInfoDO.class);
        menuInfoDO.setStatus(request.getStatus());
        return toRes(menuInfoService.save(menuInfoDO));
    }

    @Override
    public Result<Void> update(MenuMngUpdateRequest request) {
        if ((Objects.equals(request.getMenuType(), MenuTypeEnum.INNERLINK)
                || Objects.equals(request.getMenuType(), MenuTypeEnum.OUTLINK))
                && !StringUtil.ishttp(request.getPath())) {
            return requestFail("修改菜单'" + request.getMenuName() + "'失败，地址必须以http(s)://开头");
        } else if (request.getId().equals(request.getParentId())) {
            return requestFail("修改菜单'" + request.getMenuName() + "'失败，上级菜单不能选择自己");
        }
        MenuInfoDO menuInfoDO = BeanCopierUtil.copy(request, MenuInfoDO.class);
        menuInfoDO.setStatus(request.getStatus());
        return toRes(menuInfoService.updateById(menuInfoDO));
    }

    @Override
    public Result<Void> remove(IdRequest request) {
        Long menuId = request.getId();
        if (menuInfoService.existChildren(menuId)) {
            return requestFail("存在子菜单,不允许删除");
        }
        return toRes(menuInfoService.removeMenu(menuId));
    }

}
