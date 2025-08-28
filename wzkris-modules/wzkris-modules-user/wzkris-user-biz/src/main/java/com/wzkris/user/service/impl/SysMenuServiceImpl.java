package com.wzkris.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wzkris.common.core.constant.CommonConstants;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.user.constant.MenuConstants;
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
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
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

    /**
     * url query参数转map
     *
     * @param query url查询参数
     */
    public static Map<String, String> parseQuery(String query) {
        Map<String, String> result = new HashMap<>();
        if (StringUtils.isBlank(query)) {
            return result;
        }

        // 移除开头的 ? 字符
        if (query.startsWith("?")) {
            query = query.substring(1);
        }

        // 使用 & 分割参数对
        String[] pairs = StringUtils.split(query, '&');
        if (ArrayUtils.isEmpty(pairs)) {
            return result;
        }

        // 处理每个参数对
        for (String pair : pairs) {
            if (StringUtils.isBlank(pair)) {
                continue;
            }

            // 使用第一个 = 分割键值
            int idx = pair.indexOf('=');
            if (idx == -1) {
                // 无值参数，如 "param"
                String key = decodeUrlComponent(pair);
                result.put(key, "");
            } else {
                // 有值参数，如 "param=value"
                String key = decodeUrlComponent(pair.substring(0, idx));
                String value = idx < pair.length() - 1 ?
                        decodeUrlComponent(pair.substring(idx + 1)) :
                        "";
                result.put(key, value);
            }
        }

        return result;
    }

    private static String decodeUrlComponent(String encoded) {
        try {
            // 使用 UTF-8 解码
            return java.net.URLDecoder.decode(encoded, StandardCharsets.UTF_8);
        } catch (Exception e) {
            // 通常不会发生，因为 UTF-8 是标准字符集
            return encoded;
        }
    }

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
        return menuMapper.listPermsByMenuIds(menuIds).stream()
                .filter(StringUtil::isNotBlank)
                .toList();
    }

    @Override
    public List<String> listPermsByTenantPackageId(Long tenantPackageId) {
        // 查出套餐绑定的所有菜单
        List<Long> menuIds = tenantPackageMapper.listMenuIdByPackageId(tenantPackageId);
        return listPermsByMenuIds(menuIds);
    }

    @Override
    public List<SelectTreeVO> listSelectTree(Long userId) {
        List<Long> menuIds = null;
        if (!SysUser.isSuperAdmin(userId)) {
            // 去关联表中查绑定的菜单ID
            Long tenantPackageId = tenantMapper.selectPackageIdByUserId(userId);
            if (tenantPackageId != null) {
                // 租户最高管理员，去查套餐绑定菜单
                menuIds = tenantPackageMapper.listMenuIdByPackageId(tenantPackageId);
            } else {
                menuIds = this.listMenuIdByUserId(userId);
            }
            if (CollectionUtils.isEmpty(menuIds)) {
                return Collections.emptyList();
            }
        }
        LambdaQueryWrapper<SysMenu> lqw = Wrappers.lambdaQuery(SysMenu.class)
                .eq(SysMenu::getStatus, CommonConstants.STATUS_ENABLE)
                .in(CollectionUtils.isNotEmpty(menuIds), SysMenu::getMenuId, menuIds)
                .orderByDesc(SysMenu::getMenuSort, SysMenu::getMenuId);
        return this.buildSelectTree(menuMapper.selectList(lqw));
    }

    @Override
    public List<RouterVO> listRouter(Long userId) {
        List<Long> menuIds = null;
        if (!SysUser.isSuperAdmin(userId)) {
            // 去关联表中查绑定的菜单ID
            Long tenantPackageId = tenantMapper.selectPackageIdByUserId(userId);
            if (tenantPackageId != null) {
                // 租户最高管理员，去查套餐绑定菜单
                menuIds = tenantPackageMapper.listMenuIdByPackageId(tenantPackageId);
            } else {
                menuIds = this.listMenuIdByUserId(userId);
            }
            if (CollectionUtils.isEmpty(menuIds)) {
                return Collections.emptyList();
            }
        }
        List<SysMenu> list = this.getChildNode(menuMapper.listRouter(menuIds), 0);
        return this.buildRouter(list);
    }

    /**
     * 查询用户对应菜单id
     */
    public List<Long> listMenuIdByUserId(Long userId) {
        List<Long> roleIds = roleService.listInheritedIdByUserId(userId);
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
    private List<RouterVO> buildRouter(@Nullable List<SysMenu> menus) {
        List<RouterVO> routers = new LinkedList<>();

        if (CollectionUtils.isEmpty(menus)) {
            return routers;
        }

        // 排序菜单
        List<SysMenu> sortedMenus = menus.stream()
                .sorted(Comparator.comparing(SysMenu::getMenuSort, Comparator.reverseOrder())
                        .thenComparing(SysMenu::getMenuId, Comparator.reverseOrder()))
                .toList();

        for (SysMenu menu : sortedMenus) {
            RouterVO router = new RouterVO();
            router.setPath(menu.getPath());
            router.setComponent(menu.getComponent());

            // 构建meta信息
            MetaVO meta = new MetaVO(
                    menu.getMenuName(),
                    menu.getMenuType(),
                    menu.getIcon(),
                    !menu.getVisible(),
                    menu.getCacheable(),
                    parseQuery(menu.getQuery())
            );

            // 处理菜单类型
            if (StringUtil.equals(MenuConstants.TYPE_DIR, menu.getMenuType())) {
                router.setChildren(this.buildRouter(menu.getChildren()));
            } else if (StringUtil.equalsAny(
                    menu.getMenuType(), MenuConstants.TYPE_OUTLINK, MenuConstants.TYPE_INNERLINK)) {
                meta.setLink(menu.getPath());
                router.setPath(menu.getMenuName());
            }

            router.setMeta(meta);
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
    public boolean checkExistSubMenu(Long menuId) {
        LambdaQueryWrapper<SysMenu> lqw = Wrappers.lambdaQuery(SysMenu.class).eq(SysMenu::getParentId, menuId);
        return menuMapper.exists(lqw);
    }

    @Override
    public boolean checkExistRole(Long menuId) {
        return roleMenuMapper.existByMenuId(menuId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteById(Long menuId) {
        boolean success = menuMapper.deleteById(menuId) > 0;
        if (success) {
            roleMenuMapper.deleteByMenuId(menuId);
        }
        return success;
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
