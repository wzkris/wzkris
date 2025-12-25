package com.wzkris.usercenter.service;

import com.wzkris.usercenter.domain.RoleInfoDO;
import jakarta.annotation.Nullable;

import java.util.List;

/**
 * 角色业务层
 *
 * @author wzkris
 */
public interface RoleInfoService {

    /**
     * 根据管理员ID查询关联角色(正常状态)
     *
     * @param adminId 管理员ID
     * @return 角色列表
     */
    List<RoleInfoDO> listByAdminId(Long adminId);

    /**
     * 根据管理员ID查询关联角色ID(正常状态)
     *
     * @param adminId 管理员ID
     * @return 角色列表
     */
    List<Long> listIdByAdminId(Long adminId);

    /**
     * 根据管理员ID查询关联角色(正常状态)包含继承角色
     *
     * @param adminId 管理员ID
     * @return 角色列表
     */
    List<RoleInfoDO> listInheritedByAdminId(Long adminId);

    /**
     * 根据管理员ID查询关联角色ID(正常状态)包含继承角色
     *
     * @param adminId 管理员ID
     * @return 角色列表
     */
    List<Long> listInheritedIdByAdminId(Long adminId);

    /**
     * 获取当前角色组
     */
    String getRoleGroup();

    /**
     * 新增角色信息
     *
     * @param role         角色信息
     * @param menuIds      菜单组
     * @param deptIds      部门组
     * @param inheritedIds 继承角色组
     */
    boolean saveRole(RoleInfoDO role, @Nullable List<Long> menuIds, @Nullable List<Long> deptIds, @Nullable List<Long> inheritedIds);

    /**
     * 修改角色信息
     *
     * @param role         角色信息
     * @param menuIds      菜单组
     * @param deptIds      部门组
     * @param inheritedIds 继承角色组
     */
    boolean modifyRole(RoleInfoDO role, @Nullable List<Long> menuIds, @Nullable List<Long> deptIds, @Nullable List<Long> inheritedIds);

    /**
     * 角色分配管理员
     *
     * @param roleId   角色ID
     * @param adminIds 管理员ID
     */
    boolean grantAdmins(Long roleId, List<Long> adminIds);

    /**
     * 角色取消分配
     *
     * @param roleId   角色ID
     * @param adminIds 管理员ID
     */
    boolean ungrantAdmins(Long roleId, List<Long> adminIds);

    /**
     * 批量删除角色信息
     *
     * @param roleIds 需要删除的角色ID
     */
    boolean removeByIds(List<Long> roleIds);

    /**
     * 校验角色是否被管理员关联
     *
     * @param roleIds 角色组
     */
    void existAdmin(List<Long> roleIds);

    /**
     * 校验是否存在继承关系
     *
     * @param roleIds 角色组
     */
    void existInherited(List<Long> roleIds);

    /**
     * 校验角色是否可以被继承
     *
     * @param roleId       角色ID
     * @param inheritedIds 被继承角色ID
     */
    void checkInheritedRole(@Nullable Long roleId, @Nullable List<Long> inheritedIds);

}
