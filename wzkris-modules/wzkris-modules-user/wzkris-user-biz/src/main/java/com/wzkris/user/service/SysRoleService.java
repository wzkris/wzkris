package com.wzkris.user.service;

import com.wzkris.user.domain.SysRole;
import jakarta.annotation.Nullable;

import java.util.List;

/**
 * 角色业务层
 *
 * @author wzkris
 */
public interface SysRoleService {

    /**
     * 根据用户ID查询关联角色(正常状态)
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    List<SysRole> listByUserId(Long userId);

    /**
     * 根据用户ID查询关联角色ID(正常状态)
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    List<Long> listIdByUserId(Long userId);

    /**
     * 获取当前角色组
     */
    String getRoleGroup();

    /**
     * 新增角色信息
     *
     * @param role    角色信息
     * @param menuIds 菜单组
     * @param deptIds 部门组
     */
    boolean insertRole(SysRole role, @Nullable List<Long> menuIds, @Nullable List<Long> deptIds);

    /**
     * 修改角色信息
     *
     * @param role    角色信息
     * @param menuIds 菜单组
     * @param deptIds 部门组
     */
    boolean updateRole(SysRole role, @Nullable List<Long> menuIds, @Nullable List<Long> deptIds);

    /**
     * 批量授权用户
     *
     * @param roleId  角色ID
     * @param userIds 需要删除的用户数据ID
     */
    boolean allocateUsers(Long roleId, List<Long> userIds);

    /**
     * 批量删除角色信息
     *
     * @param roleIds 需要删除的角色ID
     */
    boolean deleteByIds(List<Long> roleIds);

    /**
     * 校验角色是否被用户关联
     *
     * @param roleIds 角色组
     */
    void checkRoleUsed(List<Long> roleIds);

}
