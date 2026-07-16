package com.wzkris.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.orm.plus.ServiceImplPlus;
import com.wzkris.usercenter.api.menu.response.MetaResponse;
import com.wzkris.usercenter.api.menu.response.RouterResponse;
import com.wzkris.usercenter.domain.AdminInfoDO;
import com.wzkris.usercenter.domain.MenuInfoDO;
import com.wzkris.usercenter.domain.PostInfoDO;
import com.wzkris.usercenter.domain.RoleInfoDO;
import com.wzkris.usercenter.domain.TenantInfoDO;
import com.wzkris.usercenter.enums.menu.MenuScopeEnum;
import com.wzkris.usercenter.enums.menu.MenuStatusEnum;
import com.wzkris.usercenter.enums.menu.MenuTypeEnum;
import com.wzkris.usercenter.mapper.MenuInfoMapper;
import com.wzkris.usercenter.mapper.PostToMenuMapper;
import com.wzkris.usercenter.mapper.RoleToMenuMapper;
import com.wzkris.usercenter.mapper.TenantPackageInfoMapper;
import com.wzkris.usercenter.response.SelectTreeResponse;
import com.wzkris.usercenter.service.MenuInfoService;
import com.wzkris.usercenter.service.PostInfoService;
import com.wzkris.usercenter.service.RoleInfoService;
import com.wzkris.usercenter.service.TenantInfoService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
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
public class MenuInfoServiceImpl
        extends ServiceImplPlus<MenuInfoMapper, MenuInfoDO>
        implements MenuInfoService {

    private final TenantInfoService tenantInfoService;

    private final TenantPackageInfoMapper tenantPackageInfoMapper;

    private final RoleInfoService roleInfoService;

    private final RoleToMenuMapper roleToMenuMapper;

    private final PostInfoService postInfoService;

    private final PostToMenuMapper postToMenuMapper;

    /**
     * url query参数转map
     *
     * @param query url查询参数
     */
    private static Map<String, String> parseQuery(String query) {
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
        List<Long> menuIds = roleToMenuMapper.listMenuIdByRoleIds(roleIds);
        if (CollectionUtils.isEmpty(menuIds)) {
            return Collections.emptyList();
        }
        return this.listPermsByMenuIds(menuIds);
    }

    @Override
    public List<String> listPermsByPostIds(List<Long> postIds) {
        if (CollectionUtils.isEmpty(postIds)) {
            return Collections.emptyList();
        }
        List<Long> menuIds = postToMenuMapper.listMenuIdByPostIds(postIds);
        if (CollectionUtils.isEmpty(menuIds)) {
            return Collections.emptyList();
        }
        return this.listPermsByMenuIds(menuIds);
    }

    @Override
    public List<String> listPermsByMenuIds(@Nullable List<Long> menuIds) {
        return this.listObjs(Wrappers.lambdaQuery(this.getEntityClass())
                        .select(MenuInfoDO::getPerms)
                        .in(ObjectUtils.isNotEmpty(menuIds), MenuInfoDO::getMenuId, menuIds)
                        .eq(MenuInfoDO::getStatus, MenuStatusEnum.ENABLE), Object::toString)
                .stream()
                .filter(StringUtil::isNotBlank)
                .distinct()
                .toList();
    }

    @Override
    public List<String> listPermsByTenantPackageId(Long tenantPackageId) {
        // 查出套餐绑定的所有菜单
        List<Long> menuIds = tenantPackageInfoMapper.listMenuIdByPackageId(tenantPackageId);
        if (CollectionUtils.isEmpty(menuIds)) {
            return Collections.emptyList();
        }
        return listPermsByMenuIds(menuIds);
    }

    @Override
    public List<SelectTreeResponse> listSystemSelectTree(Long adminId) {
        List<Long> menuIds = null;
        if (!AdminInfoDO.isSuperAdmin(adminId)) {
            menuIds = this.listMenuIdByAdminId(adminId);
            if (CollectionUtils.isEmpty(menuIds)) {
                return Collections.emptyList();
            }
        }
        LambdaQueryWrapper<MenuInfoDO> lqw = Wrappers.lambdaQuery(MenuInfoDO.class)
                .eq(MenuInfoDO::getStatus, MenuStatusEnum.ENABLE)
                .eq(MenuInfoDO::getScope, MenuScopeEnum.SYSTEM.getValue())
                .in(CollectionUtils.isNotEmpty(menuIds), MenuInfoDO::getMenuId, menuIds)
                .orderByDesc(MenuInfoDO::getMenuSort, MenuInfoDO::getMenuId);
        return this.buildSelectTree(baseMapper.selectList(lqw));
    }

    @Override
    public List<SelectTreeResponse> listTenantSelectTree(Long memberId) {
        List<Long> menuIds;
        Long tenantPackageId = tenantInfoService.getObjByObj(TenantInfoDO::getPackageId,
                TenantInfoDO::getAdministrator, memberId);
        if (tenantPackageId != null) {
            // 租户最高管理员，去查套餐绑定菜单
            menuIds = tenantPackageInfoMapper.listMenuIdByPackageId(tenantPackageId);
        } else {
            menuIds = this.listMenuIdByMemberId(memberId);
        }
        if (CollectionUtils.isEmpty(menuIds)) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<MenuInfoDO> lqw = Wrappers.lambdaQuery(MenuInfoDO.class)
                .eq(MenuInfoDO::getStatus, MenuStatusEnum.ENABLE)
                .eq(MenuInfoDO::getScope, MenuScopeEnum.TENANT.getValue())
                .in(CollectionUtils.isNotEmpty(menuIds), MenuInfoDO::getMenuId, menuIds)
                .orderByDesc(MenuInfoDO::getMenuSort, MenuInfoDO::getMenuId);
        return this.buildSelectTree(baseMapper.selectList(lqw));
    }

    @Override
    public List<SelectTreeResponse> listAllTenantSelectTree() {
        LambdaQueryWrapper<MenuInfoDO> lqw = Wrappers.lambdaQuery(MenuInfoDO.class)
                .eq(MenuInfoDO::getStatus, MenuStatusEnum.ENABLE)
                .eq(MenuInfoDO::getScope, MenuScopeEnum.TENANT.getValue())
                .orderByDesc(MenuInfoDO::getMenuSort, MenuInfoDO::getMenuId);
        return this.buildSelectTree(baseMapper.selectList(lqw));
    }

    @Override
    public List<RouterResponse> listSystemRoutes(Long adminId) {
        List<Long> menuIds = null;
        if (!AdminInfoDO.isSuperAdmin(adminId)) {
            menuIds = this.listMenuIdByAdminId(adminId);
            if (CollectionUtils.isEmpty(menuIds)) {
                return Collections.emptyList();
            }
        }
        List<MenuInfoDO> list = listVisibleMenus(menuIds, MenuScopeEnum.SYSTEM);
        return this.buildRouterTree(list);
    }

    @Override
    public List<RouterResponse> listTenantRoutes(Long memberId) {
        // 去关联表中查绑定的菜单ID
        List<Long> menuIds;
        Long tenantPackageId = tenantInfoService.getObjByObj(TenantInfoDO::getPackageId,
                TenantInfoDO::getAdministrator, memberId);
        if (tenantPackageId != null) {
            // 户最高管理员，去查套餐绑定菜单租
            menuIds = tenantPackageInfoMapper.listMenuIdByPackageId(tenantPackageId);
        } else {
            menuIds = this.listMenuIdByMemberId(memberId);
        }
        if (CollectionUtils.isEmpty(menuIds)) {
            return Collections.emptyList();
        }
        List<MenuInfoDO> list = listVisibleMenus(menuIds, MenuScopeEnum.TENANT);
        return this.buildRouterTree(list);
    }

    /**
     * 查询前端可见的菜单路由（按钮除外）
     *
     * @param menuIds 菜单ID列表，null表示不限制
     * @param scope   菜单域
     * @return 菜单列表
     */
    private List<MenuInfoDO> listVisibleMenus(List<Long> menuIds, MenuScopeEnum scope) {
        return baseMapper.selectList(Wrappers.lambdaQuery(MenuInfoDO.class)
                .in(MenuInfoDO::getMenuType, MenuTypeEnum.DIR, MenuTypeEnum.MENU, MenuTypeEnum.INNERLINK, MenuTypeEnum.OUTLINK)
                .eq(MenuInfoDO::getStatus, MenuStatusEnum.ENABLE)
                .eq(MenuInfoDO::getScope, scope)
                .eq(MenuInfoDO::getVisible, true)
                .in(CollectionUtils.isNotEmpty(menuIds), MenuInfoDO::getMenuId, menuIds)
                .orderByDesc(MenuInfoDO::getMenuSort, MenuInfoDO::getMenuId));
    }

    /**
     * 查询用户对应菜单id
     */
    @Override
    public List<Long> listMenuIdByAdminId(Long adminId) {
        List<Long> roleIds = roleInfoService.listByAdminId(adminId, true).stream()
                .map(RoleInfoDO::getRoleId).toList();
        if (CollectionUtils.isEmpty(roleIds)) {
            return Collections.emptyList();
        }
        return roleToMenuMapper.listMenuIdByRoleIds(roleIds);
    }

    @Override
    public List<Long> listMenuIdByMemberId(Long memberId) {
        List<Long> postIds = postInfoService.listByMemberId(memberId).stream()
                .map(PostInfoDO::getPostId).toList();
        if (CollectionUtils.isEmpty(postIds)) {
            return Collections.emptyList();
        }
        return postToMenuMapper.listMenuIdByPostIds(postIds);
    }

    @Override
    public List<Long> listMenuIdByRoleId(@Nullable Long roleId) {
        if (roleId == null) {
            return Collections.emptyList();
        }
        return roleToMenuMapper.listMenuIdByRoleIds(Collections.singletonList(roleId));
    }

    @Override
    public List<Long> listMenuIdByPostId(@Nullable Long postId) {
        if (postId == null) {
            return Collections.emptyList();
        }
        return postToMenuMapper.listMenuIdByPostIds(Collections.singletonList(postId));
    }

    /**
     * 构建路由树结构
     *
     * @param menus 菜单列表
     * @return 路由列表
     */
    private List<RouterResponse> buildRouterTree(List<MenuInfoDO> menus) {
        if (menus == null || menus.isEmpty()) {
            return new ArrayList<>(1);
        }

        Set<Long> allMenuIds = new HashSet<>(menus.size());
        Map<Long, List<MenuInfoDO>> childMap = new HashMap<>(menus.size());

        for (MenuInfoDO menu : menus) {
            allMenuIds.add(menu.getMenuId());
            childMap.computeIfAbsent(menu.getParentId(), k -> new ArrayList<>())
                    .add(menu);
        }

        List<MenuInfoDO> rootNodes = menus.stream()
                .filter(menu -> !allMenuIds.contains(menu.getParentId()))
                .toList();

        return this.buildRouter(rootNodes, childMap);
    }

    /**
     * 构建路由，显示顺序需要跟排序一致
     *
     * @param menus    菜单列表
     * @param childMap 子菜单映射
     * @return 路由列表
     */
    private List<RouterResponse> buildRouter(@Nullable List<MenuInfoDO> menus, Map<Long, List<MenuInfoDO>> childMap) {
        List<RouterResponse> routers = new LinkedList<>();

        if (CollectionUtils.isEmpty(menus)) {
            return routers;
        }

        // 排序菜单
        List<MenuInfoDO> sortedMenus = menus.stream()
                .sorted(Comparator.comparing(MenuInfoDO::getMenuSort, Comparator.reverseOrder())
                        .thenComparing(MenuInfoDO::getMenuId, Comparator.reverseOrder()))
                .toList();

        for (MenuInfoDO menu : sortedMenus) {
            RouterResponse router = new RouterResponse();
            router.setPath(menu.getPath());
            router.setComponent(menu.getComponent());

            // 构建meta信息
            MetaResponse meta = new MetaResponse(
                    menu.getMenuName(),
                    menu.getMenuType(),
                    menu.getIcon(),
                    !menu.getVisible(),
                    menu.getCacheable(),
                    parseQuery(menu.getQuery())
            );

            // 处理菜单类型
            if (Objects.equals(MenuTypeEnum.DIR, menu.getMenuType())) {
                List<MenuInfoDO> children = childMap.get(menu.getMenuId());
                router.setChildren(this.buildRouter(children, childMap));
            } else if (Objects.equals(menu.getMenuType(), MenuTypeEnum.INNERLINK)
                    || Objects.equals(menu.getMenuType(), MenuTypeEnum.OUTLINK)) {
                meta.setLink(menu.getPath());
                router.setPath(menu.getMenuName());
            }

            router.setMeta(meta);
            routers.add(router);
        }

        return routers;
    }

    private List<SelectTreeResponse> buildSelectTree(List<MenuInfoDO> menus) {
        if (menus == null || menus.isEmpty()) {
            return new ArrayList<>(1);
        }

        Set<Long> allMenuIds = new HashSet<>(menus.size());
        Map<Long, List<MenuInfoDO>> childMap = new HashMap<>(menus.size());

        for (MenuInfoDO menu : menus) {
            allMenuIds.add(menu.getMenuId());
            childMap.computeIfAbsent(menu.getParentId(), k -> new ArrayList<>())
                    .add(menu);
        }

        List<MenuInfoDO> rootNodes = menus.stream()
                .filter(menu -> !allMenuIds.contains(menu.getParentId()))
                .toList();

        return rootNodes.stream()
                .map(root -> convertToSelectTreeResp(root, childMap))
                .collect(Collectors.toList());
    }

    private SelectTreeResponse convertToSelectTreeResp(MenuInfoDO menu, Map<Long, List<MenuInfoDO>> childMap) {
        SelectTreeResponse response = new SelectTreeResponse();
        response.setId(menu.getMenuId());
        response.setLabel(menu.getMenuName());

        List<MenuInfoDO> children = childMap.get(menu.getMenuId());
        // 递归转换子节点
        if (children != null && !children.isEmpty()) {
            List<SelectTreeResponse> childrenResp = children.stream()
                    .map(child -> convertToSelectTreeResp(child, childMap)) // 递归调用
                    .collect(Collectors.toList());
            response.setChildren(childrenResp);
        }
        return response;
    }

    @Override
    public boolean existChildren(Long menuId) {
        LambdaQueryWrapper<MenuInfoDO> lqw = Wrappers.lambdaQuery(MenuInfoDO.class).eq(MenuInfoDO::getParentId, menuId);
        return baseMapper.exists(lqw);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeMenu(Long menuId) {
        boolean success = baseMapper.deleteById(menuId) > 0;
        if (success) {
            roleToMenuMapper.deleteByMenuId(menuId);
            postToMenuMapper.deleteByMenuId(menuId);
        }
        return success;
    }

}

