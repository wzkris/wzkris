package com.wzkris.usercenter.service;

import com.wzkris.common.orm.plus.IServicePlus;
import com.wzkris.usercenter.api.menu.response.RouterResponse;
import com.wzkris.usercenter.domain.MenuInfoDO;
import com.wzkris.usercenter.response.SelectTreeResponse;
import org.springframework.lang.Nullable;

import java.util.List;

/**
 * 菜单 业务层
 *
 * @author wzkris
 */
public interface MenuInfoService extends IServicePlus<MenuInfoDO> {

    /**
     * 根据角色ID集合查询权限
     *
     * @param roleIds 角色ID集合
     * @return 权限列表
     */
    List<String> listPermsByRoleIds(@Nullable List<Long> roleIds);

    /**
     * 根据角色ID集合查询权限
     *
     * @param tenantRoleIds 角色ID集合
     * @return 权限列表
     */
    List<String> listPermsByTenantRoleIds(@Nullable List<Long> tenantRoleIds);

    /**
     * 根据菜单ID集合查询权限
     *
     * @param menuIds 菜单ID集合
     * @return 权限列表
     */
    List<String> listPermsByMenuIds(@Nullable List<Long> menuIds);

    /**
     * 根据套餐查询权限
     *
     * @param tenantPackageId 租户套餐ID
     * @return 权限列表
     */
    List<String> listPermsByTenantPackageId(@Nullable Long tenantPackageId);

    /**
     * 查询系统菜单选择树
     *
     * @param adminId 管理员ID
     * @return 菜单列表
     */
    List<SelectTreeResponse> listSystemSelectTree(Long adminId);

    /**
     * 查询租户菜单选择树
     *
     * @param tenantUserId 用户ID
     * @return 菜单列表
     */
    List<SelectTreeResponse> listTenantSelectTree(Long tenantUserId);

    /**
     * 查询所有租户菜单选择树
     *
     * @return 菜单列表
     */
    List<SelectTreeResponse> listAllTenantSelectTree();

    /**
     * 根据管理员ID查询系统路由
     *
     * @param adminId 管理员ID
     * @return 前端路由
     */
    List<RouterResponse> listSystemRoutes(Long adminId);

    /**
     * 根据租户用户ID查询租户路由
     *
     * @param tenantUserId 用户ID
     * @return 前端路由
     */
    List<RouterResponse> listTenantRoutes(Long tenantUserId);

    /**
     * 查询管理员对应菜单id
     *
     * @param adminId 管理员ID
     * @return 菜单ID
     */
    List<Long> listMenuIdByAdminId(Long adminId);

    /**
     * 查询租户租户用户对应菜单id
     *
     * @param tenantUserId 用户ID
     * @return 菜单ID
     */
    List<Long> listMenuIdByTenantUserId(Long tenantUserId);

    /**
     * 根据角色ID查询菜单ID
     *
     * @param roleId 角色ID
     * @return 菜单ID
     */
    List<Long> listMenuIdByRoleId(@Nullable Long roleId);

    /**
     * 根据角色ID查询菜单ID
     *
     * @param tenantRoleId 角色ID
     * @return 菜单ID
     */
    List<Long> listMenuIdByTenantRoleId(@Nullable Long tenantRoleId);

    /**
     * 是否存在菜单子节点
     *
     * @param menuId 菜单ID
     * @return 结果 true 存在 false 不存在
     */
    boolean existChildren(Long menuId);

    /**
     * 删除菜单
     *
     * @param menuId 菜单ID
     */
    boolean removeMenu(Long menuId);

}

