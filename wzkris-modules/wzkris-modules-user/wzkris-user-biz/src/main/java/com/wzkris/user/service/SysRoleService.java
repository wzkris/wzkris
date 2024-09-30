package com.wzkris.user.service;

import com.wzkris.user.domain.SysRole;
import com.wzkris.user.domain.dto.SysRoleDTO;
import org.springframework.lang.Nullable;

import java.util.List;

/**
 * 角色业务层
 *
 * @author wzkris
 */
public interface SysRoleService {

    /**
     * 根据条件查询角色列表
     *
     * @param role 条件
     * @return 角色列表
     */
    List<SysRole> list(SysRole role);

    /**
     * 根据用户ID查询角色列表
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    List<SysRole> listByUserId(@Nullable Long userId);

    /**
     * 根据用户id获取角色
     */
    String getRoleGroup(@Nullable Long userId);

    /**
     * 新增保存角色信息
     *
     * @param roleDTO 角色信息
     * @return 结果
     */
    int insertRole(SysRoleDTO roleDTO);

    /**
     * 修改保存角色信息
     *
     * @param roleDTO 角色信息
     * @return 结果
     */
    int updateRole(SysRoleDTO roleDTO);

    /**
     * 批量授权用户
     *
     * @param roleId  角色ID
     * @param userIds 需要删除的用户数据ID
     */
    void allocateUsers(Long roleId, List<Long> userIds);

    /**
     * 修改部门数据权限信息
     *
     * @param roleDTO 角色信息
     * @return 结果
     */
    int updateDeptScope(SysRoleDTO roleDTO);

    /**
     * 批量删除角色信息
     *
     * @param roleIds 需要删除的角色ID
     * @return 结果
     */
    int deleteByIds(List<Long> roleIds);

    /**
     * 校验角色是否被用户关联
     *
     * @param roleIds 角色组
     */
    void checkUserUse(List<Long> roleIds);

    /**
     * 校验是否有角色的数据权限
     *
     * @param roleIds 待操作的角色id数组
     */
    void checkDataScopes(List<Long> roleIds);

    /**
     * 校验是否有角色的数据权限
     *
     * @param roleId 待操作的角色id
     */
    default void checkDataScopes(Long roleId) {
        this.checkDataScopes(List.of(roleId));
    }
}
