package com.wzkris.user.service;

import com.wzkris.user.domain.SysRole;
import com.wzkris.user.domain.dto.SysRoleDTO;
import jakarta.annotation.Nullable;

import java.util.Collections;
import java.util.List;

/**
 * 角色业务层
 *
 * @author wzkris
 */
public interface SysRoleService {

    /**
     * 查询可授权角色
     *
     * @return 角色列表
     */
    List<SysRole> listCanGranted();

    /**
     * 根据用户ID查询关联角色(正常状态)
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    List<SysRole> listByUserId(@Nullable Long userId);

    /**
     * 根据用户ID查询关联角色ID(正常状态)
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    List<Long> listIdByUserId(@Nullable Long userId);

    /**
     * 获取当前角色组
     */
    String getRoleGroup();

    /**
     * 新增角色信息
     *
     * @param roleDTO 角色信息
     */
    void insertRole(SysRoleDTO roleDTO);

    /**
     * 修改角色信息
     *
     * @param roleDTO 角色信息
     */
    void updateRole(SysRoleDTO roleDTO);

    /**
     * 批量授权用户
     *
     * @param roleId  角色ID
     * @param userIds 需要删除的用户数据ID
     */
    void allocateUsers(Long roleId, List<Long> userIds);

    /**
     * 批量删除角色信息
     *
     * @param roleIds 需要删除的角色ID
     */
    void deleteByIds(List<Long> roleIds);

    /**
     * 校验角色是否被用户关联
     *
     * @param roleIds 角色组
     */
    void checkRoleUse(List<Long> roleIds);

    /**
     * 校验是否有角色的数据权限
     *
     * @param roleIds 待操作的角色id数组
     */
    void checkDataScopes(List<Long> roleIds);

    default void checkDataScopes(Long roleId) {
        this.checkDataScopes(Collections.singletonList(roleId));
    }

}
