package com.wzkris.user.service;

import com.wzkris.user.domain.SysUser;
import com.wzkris.user.domain.dto.SysUserDTO;

import java.util.Collections;
import java.util.List;

/**
 * 管理员 业务层
 *
 * @author wzkris
 */
public interface SysUserService {
    /**
     * 列表查询
     *
     * @param user 筛选条件
     */
    List<SysUser> list(SysUser user);

    /**
     * 根据条件分页查询已分配用户角色列表
     *
     * @param roleId 管理员信息
     * @return 管理员信息集合信息
     */
    List<SysUser> listAllocated(SysUser user, Long roleId);

    /**
     * 根据条件分页查询未分配用户角色列表
     *
     * @param roleId 管理员信息
     * @return 管理员信息集合信息
     */
    List<SysUser> listUnallocated(SysUser user, Long roleId);

    /**
     * 新增管理员信息
     *
     * @param dto 管理员信息
     */
    void insertUser(SysUserDTO dto);

    /**
     * 修改管理员信息
     *
     * @param dto 管理员信息
     */
    void updateUser(SysUserDTO dto);

    /**
     * 硬删除用户
     *
     * @param userIds 用户ID
     */
    void hardDeleteByIds(List<Long> userIds);

    /**
     * 批量授权角色
     *
     * @param userId  管理员ID
     * @param roleIds 角色组
     */
    void allocateRoles(Long userId, List<Long> roleIds);

    /**
     * 校验用户是否唯一
     *
     * @param user 筛选条件
     */
    boolean checkUserUnique(SysUser user);

    /**
     * 校验是否有数据权限
     *
     * @param userIds 被操作的对象id
     */
    void checkDataScopes(List<Long> userIds);

    /**
     * 校验是否有数据权限
     *
     * @param userId 被操作的对象id
     */
    default void checkDataScopes(Long userId) {
        this.checkDataScopes(Collections.singletonList(userId));
    }

}
