package com.wzkris.user.service;


import com.wzkris.user.domain.SysMenu;
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
     * 查询系统菜单列表
     *
     * @return 菜单列表
     */
    List<SysMenu> list();

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
     * 查询菜单选择树
     *
     * @return 菜单列表
     */
    List<SelectTreeVO> listMenuSelectTree(SysMenu menu);

    /**
     * 根据用户ID查询前端路由
     *
     * @param userId 用户ID
     * @return 前端路由
     */
    List<RouterVO> listRouteTree(Long userId);

    /**
     * 构建路由
     *
     * @param menus 菜单列表
     * @return 路由列表
     */
    List<RouterVO> buildRouter(List<SysMenu> menus);

    /**
     * 构建树结构
     *
     * @param menus 菜单列表
     * @return 树结构列表
     */
    List<SysMenu> buildTree(List<SysMenu> menus);

    /**
     * 构建前端所需要下拉树结构
     *
     * @param menus 菜单列表
     * @return 下拉树结构列表
     */
    List<SelectTreeVO> buildSelectTree(List<SysMenu> menus);

    /**
     * 是否存在菜单子节点
     *
     * @param menuId 菜单ID
     * @return 结果 true 存在 false 不存在
     */
    boolean hasChildByMenuId(Long menuId);

    /**
     * 查询菜单是否存在角色
     *
     * @param menuId 菜单ID
     * @return 结果 true 存在 false 不存在
     */
    boolean checkMenuExistRole(Long menuId);
}
