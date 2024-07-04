package com.wzkris.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.security.utils.SysUtil;
import com.wzkris.user.api.domain.vo.MetaVO;
import com.wzkris.user.api.domain.vo.RouterVO;
import com.wzkris.user.constant.UserConstants;
import com.wzkris.user.domain.SysMenu;
import com.wzkris.user.domain.SysUser;
import com.wzkris.user.domain.vo.SelectTree;
import com.wzkris.user.mapper.*;
import com.wzkris.user.service.SysMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
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
    private final SysMenuMapper sysMenuMapper;
    private final SysTenantMapper sysTenantMapper;
    private final SysTenantPackageMapper sysTenantPackageMapper;
    private final SysUserRoleMapper sysUserRoleMapper;
    private final SysRoleMenuMapper sysRoleMenuMapper;

    /**
     * 查询系统菜单列表
     *
     * @param menu 菜单信息
     * @return 菜单列表
     */
    @Override
    public List<SysMenu> list(SysMenu menu) {
        LambdaQueryWrapper<SysMenu> lqw = this.buildQueryWrapper(menu);
        List<SysMenu> menuList;
        Long userId = SysUtil.getUserId();

        if (SysUser.isSuperAdmin(userId)) {
            // 超管查全部
            menuList = sysMenuMapper.selectList(lqw);
        }
        else {
            // 去关联表中查绑定的菜单ID
            List<Long> menuIds;
            Long tenantPackageId = sysTenantMapper.selectPackageIdByUserId(userId);
            if (tenantPackageId != null) {
                // 租户最高管理员，去查套餐绑定菜单
                menuIds = sysTenantPackageMapper.listMenuIdByPackageId(tenantPackageId);
            }
            else {
                menuIds = this.listMenuIdByUserId(userId);
            }
            if (CollectionUtils.isEmpty(menuIds)) {
                return Collections.emptyList();
            }
            lqw.in(SysMenu::getMenuId, menuIds);
            menuList = sysMenuMapper.selectList(lqw);
        }

        return menuList;
    }

    private LambdaQueryWrapper<SysMenu> buildQueryWrapper(SysMenu menu) {
        return new LambdaQueryWrapper<SysMenu>()
                .eq(StringUtil.isNotNull(menu.getMenuId()), SysMenu::getMenuId, menu.getMenuId())
                .like(StringUtil.isNotNull(menu.getMenuName()), SysMenu::getMenuName, menu.getMenuName())
                .eq(StringUtil.isNotNull(menu.getMenuType()), SysMenu::getMenuType, menu.getMenuType())
                .eq(StringUtil.isNotNull(menu.getIsVisible()), SysMenu::getIsVisible, menu.getIsVisible())
                .eq(StringUtil.isNotNull(menu.getStatus()), SysMenu::getStatus, menu.getStatus())
                .orderByDesc(SysMenu::getMenuSort, SysMenu::getMenuId);
    }

    /**
     * 根据角色ID集合查询权限
     *
     * @param roleIds 角色ID集合
     * @return 权限列表
     */
    @Override
    public List<String> listPermsByRoleIds(@Nullable List<Long> roleIds) {
        if (CollectionUtils.isEmpty(roleIds)) {
            return Collections.emptyList();
        }
        List<Long> menuIds = sysRoleMenuMapper.listMenuIdByRoleIds(roleIds.toArray(new Long[0]));
        return this.listPermsByMenuIds(menuIds);
    }

    /**
     * 根据菜单ID集合查询权限
     *
     * @param menuIds 菜单ID集合
     * @return 权限列表
     */
    @Override
    public List<String> listPermsByMenuIds(@Nullable List<Long> menuIds) {
        if (CollectionUtils.isEmpty(menuIds)) {
            return Collections.emptyList();
        }
        return sysMenuMapper.listPermsByMenuIds(menuIds)
                .stream()
                .filter(StringUtil::isNotBlank).toList();
    }

    /**
     * 查询菜单选择树
     *
     * @return 菜单列表
     */
    @Override
    public List<SelectTree> listMenuSelectTree(SysMenu menu) {
        List<SysMenu> list = this.list(menu);
        return this.buildSelectTree(list);
    }

    /**
     * 根据用户ID查询前端路由
     *
     * @param userId 用户ID
     * @return 前端路由
     */
    @Override
    public List<RouterVO> listRouteTree(Long userId) {
        List<SysMenu> menus;
        if (SysUser.isSuperAdmin(userId)) {
            menus = sysMenuMapper.listRouteTree(null);
        }
        else {
            // 去关联表中查绑定的菜单ID
            List<Long> menuIds;
            Long tenantPackageId = sysTenantMapper.selectPackageIdByUserId(userId);
            if (tenantPackageId != null) {
                // 租户最高管理员，去查套餐绑定菜单
                menuIds = sysTenantPackageMapper.listMenuIdByPackageId(tenantPackageId);
            }
            else {
                menuIds = this.listMenuIdByUserId(userId);
            }
            if (CollectionUtils.isEmpty(menuIds)) {
                return Collections.emptyList();
            }
            menus = sysMenuMapper.listRouteTree(menuIds);
        }
        List<SysMenu> list = this.getChildNode(menus, 0);
        return this.buildRouter(list);
    }

    /**
     * 查询用户对应菜单id
     */
    private List<Long> listMenuIdByUserId(Long userId) {
        List<Long> roleIds = sysUserRoleMapper.listRoleIdByUserId(userId);
        if (CollectionUtils.isEmpty(roleIds)) {
            return Collections.emptyList();
        }
        return sysRoleMenuMapper.listMenuIdByRoleIds(roleIds.toArray(new Long[0]));
    }

    /**
     * 构建路由，显示顺序需要跟排序一致
     *
     * @param menus 菜单列表
     * @return 路由列表
     */
    @Override
    public List<RouterVO> buildRouter(List<SysMenu> menus) {
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
    @Override
    public List<SysMenu> buildTree(List<SysMenu> menus) {
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

    /**
     * 构建前端所需要下拉树结构
     *
     * @param menus 菜单列表
     * @return 下拉树结构列表
     */
    @Override
    public List<SelectTree> buildSelectTree(List<SysMenu> menus) {
        List<SysMenu> menuTrees = this.buildTree(menus);
        return menuTrees.stream().map(SelectTree::new).collect(Collectors.toList());
    }

    /**
     * 是否存在菜单子节点
     *
     * @param menuId 菜单ID
     * @return 结果
     */
    @Override
    public boolean hasChildByMenuId(Long menuId) {
        return sysMenuMapper.hasChildByMenuId(menuId) > 0;
    }

    /**
     * 查询菜单使用数量
     *
     * @param menuId 菜单ID
     * @return 结果
     */
    @Override
    public boolean checkMenuExistRole(Long menuId) {
        return sysRoleMenuMapper.checkMenuExistRole(menuId) > 0;
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
