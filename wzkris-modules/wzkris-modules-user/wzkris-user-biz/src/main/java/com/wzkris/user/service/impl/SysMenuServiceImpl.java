package com.wzkris.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wzkris.common.core.constant.CommonConstants;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.user.constant.UserConstants;
import com.wzkris.user.domain.SysMenu;
import com.wzkris.user.domain.SysUser;
import com.wzkris.user.domain.vo.MetaVO;
import com.wzkris.user.domain.vo.RouterVO;
import com.wzkris.user.domain.vo.SelectTreeVO;
import com.wzkris.user.mapper.SysMenuMapper;
import com.wzkris.user.mapper.SysRoleMenuMapper;
import com.wzkris.user.mapper.SysTenantMapper;
import com.wzkris.user.mapper.SysTenantPackageMapper;
import com.wzkris.user.service.SysMenuService;
import com.wzkris.user.service.SysRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 菜单 业务层处理
 *
 * @author wzkris
 */
@Service
@RequiredArgsConstructor
public class SysMenuServiceImpl implements SysMenuService {
    private final SysMenuMapper menuMapper;
    private final SysTenantMapper tenantMapper;
    private final SysTenantPackageMapper tenantPackageMapper;
    private final SysRoleService roleService;
    private final SysRoleMenuMapper roleMenuMapper;

    @Override
    public List<String> listPermsByRoleIds(@Nullable List<Long> roleIds) {
        if (CollectionUtils.isEmpty(roleIds)) {
            return Collections.emptyList();
        }
        List<Long> menuIds = roleMenuMapper.listMenuIdByRoleIds(roleIds);
        return this.listPermsByMenuIds(menuIds);
    }

    @Override
    public List<String> listPermsByMenuIds(@Nullable List<Long> menuIds) {
        if (CollectionUtils.isEmpty(menuIds)) {
            return Collections.emptyList();
        }
        return menuMapper.listPermsByMenuIds(menuIds)
                .stream()
                .filter(StringUtil::isNotBlank).toList();
    }

    @Override
    public List<SelectTreeVO> listMenuSelectTree(Long userId) {
        List<Long> menuIds = null;
        if (!SysUser.isSuperAdmin(userId)) {
            // 去关联表中查绑定的菜单ID
            Long tenantPackageId = tenantMapper.selectPackageIdByUserId(userId);
            if (tenantPackageId != null) {
                // 租户最高管理员，去查套餐绑定菜单
                menuIds = tenantPackageMapper.listMenuIdByPackageId(tenantPackageId);
            }
            else {
                menuIds = this.listMenuIdByUserId(userId);
            }
            if (CollectionUtils.isEmpty(menuIds)) {
                return Collections.emptyList();
            }
        }
        LambdaQueryWrapper<SysMenu> lqw = Wrappers.lambdaQuery(SysMenu.class)
                .eq(SysMenu::getStatus, CommonConstants.STATUS_ENABLE)
                .in(StringUtil.isNotEmpty(menuIds), SysMenu::getMenuId, menuIds);
        return this.buildSelectTree(menuMapper.selectList(lqw));
    }

    @Override
    public List<RouterVO> listRouterTree(Long userId) {
        List<Long> menuIds = null;
        if (!SysUser.isSuperAdmin(userId)) {
            // 去关联表中查绑定的菜单ID
            Long tenantPackageId = tenantMapper.selectPackageIdByUserId(userId);
            if (tenantPackageId != null) {
                // 租户最高管理员，去查套餐绑定菜单
                menuIds = tenantPackageMapper.listMenuIdByPackageId(tenantPackageId);
            }
            else {
                menuIds = this.listMenuIdByUserId(userId);
            }
            if (CollectionUtils.isEmpty(menuIds)) {
                return Collections.emptyList();
            }
        }
        List<SysMenu> list = this.getChildNode(menuMapper.listRouteTree(menuIds), 0);
        return this.buildRouter(list);
    }

    /**
     * 查询用户对应菜单id
     */
    private List<Long> listMenuIdByUserId(Long userId) {
        List<Long> roleIds = roleService.listIdByUserId(userId);
        if (CollectionUtils.isEmpty(roleIds)) {
            return Collections.emptyList();
        }
        return roleMenuMapper.listMenuIdByRoleIds(roleIds);
    }

    /**
     * 构建路由，显示顺序需要跟排序一致
     *
     * @param menus 菜单列表
     * @return 路由列表
     */
    private List<RouterVO> buildRouter(List<SysMenu> menus) {
        menus = menus.stream().sorted(Comparator.comparing(SysMenu::getMenuSort, Comparator.reverseOrder()).
                thenComparing(SysMenu::getMenuId, Comparator.reverseOrder())).toList();
        List<RouterVO> routers = new LinkedList<>();
        for (SysMenu menu : menus) {
            RouterVO router = new RouterVO();
            router.setHidden(!menu.getIsVisible());
            router.setName(getRouteName(menu));
            router.setPath(getRouterPath(menu));
            router.setComponent(getComponent(menu));
            router.setQuery(menu.getQuery());
            router.setMeta(new MetaVO(menu.getMenuName(), menu.getIcon(), Boolean.FALSE.equals(menu.getIsCache()), menu.getPath()));
            List<SysMenu> cMenus = menu.getChildren().stream().sorted(Comparator.comparing(SysMenu::getMenuSort, Comparator.reverseOrder()).
                    thenComparing(SysMenu::getMenuId, Comparator.reverseOrder())).toList();
            if (!cMenus.isEmpty() && UserConstants.TYPE_DIR.equals(menu.getMenuType())) {
                router.setAlwaysShow(true);
                router.setRedirect("noRedirect");
                router.setChildren(buildRouter(cMenus));
            }
            else if (isMenuFrame(menu)) {
                router.setMeta(null);
                List<RouterVO> childrenList = new ArrayList<>();
                RouterVO children = new RouterVO();
                children.setPath(menu.getPath());
                children.setComponent(menu.getComponent());
                children.setName(StringUtil.upperFirst(menu.getPath()));
                children.setMeta(new MetaVO(menu.getMenuName(), menu.getIcon(), Boolean.FALSE.equals(menu.getIsCache()), menu.getPath()));
                children.setQuery(menu.getQuery());
                childrenList.add(children);
                router.setChildren(childrenList);
            }
            else if (menu.getParentId().intValue() == 0 && isInnerLink(menu)) {
                router.setMeta(new MetaVO(menu.getMenuName(), menu.getIcon()));
                router.setPath("/");
                List<RouterVO> childrenList = new ArrayList<>();
                RouterVO children = new RouterVO();
                children.setPath(menu.getPath());
                children.setComponent(UserConstants.INNER_LINK);
                children.setName(StringUtil.upperFirst(menu.getPath()));
                children.setMeta(new MetaVO(menu.getMenuName(), menu.getIcon(), menu.getPath()));
                childrenList.add(children);
                router.setChildren(childrenList);
            }
            routers.add(router);
        }
        return routers;
    }

    /**
     * 构建树结构
     *
     * @param menus 菜单列表
     * @return 树结构列表
     */
    private List<SysMenu> buildTree(List<SysMenu> menus) {
        List<SysMenu> returnList = new ArrayList<>();
        List<Long> tempList = menus.stream().map(SysMenu::getMenuId).toList();
        for (SysMenu menu : menus) {
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(menu.getParentId())) {
                recursionFn(menus, menu);
                returnList.add(menu);
            }
        }
        if (returnList.isEmpty()) {
            returnList = menus;
        }
        return returnList;
    }

    private List<SelectTreeVO> buildSelectTree(List<SysMenu> menus) {
        List<SysMenu> menuTrees = this.buildTree(menus);
        return menuTrees.stream().map(SelectTreeVO::new).collect(Collectors.toList());
    }

    @Override
    public boolean hasChildByMenuId(Long menuId) {
        LambdaQueryWrapper<SysMenu> lqw = Wrappers.lambdaQuery(SysMenu.class)
                .eq(SysMenu::getParentId, menuId);
        return menuMapper.exists(lqw);
    }

    @Override
    public boolean checkMenuExistRole(Long menuId) {
        return roleMenuMapper.checkMenuExistRole(menuId) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long menuId) {
        menuMapper.deleteById(menuId);
        roleMenuMapper.deleteByMenuId(menuId);
    }

    /**
     * 获取路由名称
     *
     * @param menu 菜单信息
     * @return 路由名称
     */
    public String getRouteName(SysMenu menu) {
        String routerName = StringUtil.upperFirst(menu.getPath());
        // 非外链并且是一级目录（类型为目录）
        if (isMenuFrame(menu)) {
            routerName = StringUtil.EMPTY;
        }
        return routerName;
    }

    /**
     * 获取路由地址
     *
     * @param menu 菜单信息
     * @return 路由地址
     */
    public String getRouterPath(SysMenu menu) {
        String routerPath = menu.getPath();
        // 非外链并且是一级目录（类型为目录）
        if (0 == menu.getParentId().intValue() && StringUtil.equals(UserConstants.TYPE_DIR, menu.getMenuType())
                && !menu.getIsFrame()) {
            routerPath = "/" + menu.getPath();
        }
        // 非外链并且是一级目录（类型为菜单）
        else if (isMenuFrame(menu)) {
            routerPath = "/";
        }
        return routerPath;
    }

    /**
     * 获取组件信息
     *
     * @param menu 菜单信息
     * @return 组件信息
     */
    public String getComponent(SysMenu menu) {
        String component = UserConstants.LAYOUT;
        if (StringUtil.isNotEmpty(menu.getComponent()) && !isMenuFrame(menu)) {
            component = menu.getComponent();
        }
        else if (StringUtil.isEmpty(menu.getComponent()) && menu.getParentId().intValue() != 0 && isInnerLink(menu)) {
            component = UserConstants.INNER_LINK;
        }
        else if (StringUtil.isEmpty(menu.getComponent()) && isParentView(menu)) {
            component = UserConstants.PARENT_VIEW;
        }
        return component;
    }

    /**
     * 是否为菜单内部跳转
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean isMenuFrame(SysMenu menu) {
        return menu.getParentId().intValue() == 0
                && StringUtil.equals(UserConstants.TYPE_MENU, menu.getMenuType())
                && !menu.getIsFrame();
    }

    /**
     * 是否为内链组件
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean isInnerLink(SysMenu menu) {
        return !menu.getIsFrame() && StringUtil.ishttp(menu.getPath());
    }

    /**
     * 是否为parent_view组件
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean isParentView(SysMenu menu) {
        return menu.getParentId().intValue() != 0 && StringUtil.equals(UserConstants.TYPE_DIR, menu.getMenuType());
    }

    /**
     * 根据父节点的ID获取所有子节点
     *
     * @param list     分类表
     * @param parentId 传入的父节点ID
     * @return String
     */
    public List<SysMenu> getChildNode(List<SysMenu> list, int parentId) {
        List<SysMenu> returnList = new ArrayList<>();
        for (SysMenu t : list) {
            // 一、根据传入的某个父节点ID,遍历该父节点的所有子节点
            if (t.getParentId() == parentId) {
                recursionFn(list, t);
                returnList.add(t);
            }
        }
        return returnList;
    }

    /**
     * 递归列表
     */
    private void recursionFn(List<SysMenu> list, SysMenu t) {
        // 得到子节点列表
        List<SysMenu> childList = getChildList(list, t);
        t.setChildren(childList);
        for (SysMenu tChild : childList) {
            if (hasChild(list, tChild)) {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<SysMenu> getChildList(List<SysMenu> list, SysMenu t) {
        List<SysMenu> tlist = new ArrayList<>();
        for (SysMenu n : list) {
            if (n.getParentId().longValue() == t.getMenuId().longValue()) {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<SysMenu> list, SysMenu t) {
        return !getChildList(list, t).isEmpty();
    }
}
