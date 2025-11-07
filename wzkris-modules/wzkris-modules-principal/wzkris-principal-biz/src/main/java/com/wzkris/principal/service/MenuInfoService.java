package com.wzkris.principal.service;

import com.wzkris.principal.domain.vo.RouterVO;
import com.wzkris.principal.domain.vo.SelectTreeVO;
import org.springframework.lang.Nullable;

import java.util.List;

/**
 * 菜单 业务层
 *
 * @author wzkris
 */
public interface MenuInfoService {

    /**
     * 根据角色ID集合查询权限
     *
     * @param roleIds 角色ID集合
     * @return 权限列表
     */
    List<String> listPermsByRoleIds(@Nullable List<Long> roleIds);

    /**
     * 根据职位ID集合查询权限
     *
     * @param postIds 职位ID集合
     * @return 权限列表
     */
    List<String> listPermsByPostIds(@Nullable List<Long> postIds);

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
    List<SelectTreeVO> listSystemSelectTree(Long adminId);

    /**
     * 查询租户菜单选择树
     *
     * @param staffId 员工ID
     * @return 菜单列表
     */
    List<SelectTreeVO> listTenantSelectTree(Long staffId);

    /**
     * 查询所有租户菜单选择树
     *
     * @return 菜单列表
     */
    List<SelectTreeVO> listAllTenantSelectTree();

    /**
     * 根据管理员ID查询系统路由
     *
     * @param adminId 管理员ID
     * @return 前端路由
     */
    List<RouterVO> listSystemRoutes(Long adminId);

    /**
     * 根据员工ID查询租户路由
     *
     * @param staffId 员工ID
     * @return 前端路由
     */
    List<RouterVO> listTenantRoutes(Long staffId);

    /**
     * 查询管理员对应菜单id
     *
     * @param adminId 管理员ID
     * @return 菜单ID
     */
    List<Long> listMenuIdByAdminId(Long adminId);

    /**
     * 查询租户员工对应菜单id
     *
     * @param staffId 员工ID
     * @return 菜单ID
     */
    List<Long> listMenuIdByStaffId(Long staffId);

    /**
     * 是否存在菜单子节点
     *
     * @param menuId 菜单ID
     * @return 结果 true 存在 false 不存在
     */
    boolean existSubMenu(Long menuId);

    /**
     * 删除菜单
     *
     * @param menuId 菜单ID
     */
    boolean removeById(Long menuId);

}
