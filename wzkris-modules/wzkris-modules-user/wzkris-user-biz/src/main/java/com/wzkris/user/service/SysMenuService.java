package com.wzkris.user.service;

import com.wzkris.user.domain.vo.RouterVO;
import com.wzkris.user.domain.vo.SelectTreeVO;
import org.springframework.lang.Nullable;

import java.util.List;

/**
 * 菜单 业务层
 *
 * @author wzkris
 */
public interface SysMenuService {

    /**
     * 根据角色ID集合查询权限
     *
     * @param roleIds 角色ID集合
     * @return 权限列表
     */
    List<String> listPermsByRoleIds(@Nullable List<Long> roleIds);

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
     * 查询菜单选择树
     *
     * @param userId 用户ID
     * @return 菜单列表
     */
    List<SelectTreeVO> listSelectTree(Long userId);

    /**
     * 根据用户ID查询前端路由
     *
     * @param userId 用户ID
     * @return 前端路由
     */
    List<RouterVO> listRouter(Long userId);

    /**
     * 查询用户对应菜单id
     *
     * @param userId 用户ID
     * @return 菜单ID
     */
    List<Long> listMenuIdByUserId(Long userId);

    /**
     * 是否存在菜单子节点
     *
     * @param menuId 菜单ID
     * @return 结果 true 存在 false 不存在
     */
    boolean checkExistSubMenu(Long menuId);

    /**
     * 查询菜单是否存在角色
     *
     * @param menuId 菜单ID
     * @return 结果 true 存在 false 不存在
     */
    boolean checkExistRole(Long menuId);

    /**
     * 删除菜单
     *
     * @param menuId 菜单ID
     */
    boolean deleteById(Long menuId);

}
