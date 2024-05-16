package com.thingslink.user.service;

import com.thingslink.common.orm.page.Page;
import com.thingslink.user.domain.SysUser;
import com.thingslink.user.domain.dto.SysUserDTO;
import com.thingslink.user.domain.vo.SysUserVO;

import java.util.List;

/**
 * 管理员 业务层
 *
 * @author wzkris
 */
public interface SysUserService {
    /**
     * 分页查询
     */
    Page<SysUserVO> listPage(SysUser user);

    /**
     * 根据条件分页查询已分配管理员角色列表
     *
     * @param roleId 管理员信息
     * @return 管理员信息集合信息
     */
    List<SysUser> listAllocated(SysUser user, Long roleId);

    /**
     * 根据条件分页查询未分配管理员角色列表
     *
     * @param roleId 管理员信息
     * @return 管理员信息集合信息
     */
    List<SysUser> listUnallocated(SysUser user, Long roleId);

    /**
     * 新增管理员信息
     *
     * @param dto 管理员信息
     * @return 结果
     */
    boolean insertUser(SysUserDTO dto);

    /**
     * 修改管理员信息
     *
     * @param dto 管理员信息
     * @return 结果
     */
    boolean updateUser(SysUserDTO dto);

    /**
     * 管理员授权角色
     *
     * @param userId  管理员ID
     * @param roleIds 角色组
     */
    void allocateRoles(Long userId, Long[] roleIds);

    /**
     * 校验是否有数据权限
     *
     * @param userIds 被操作的对象id
     */
    void checkDataScopes(Long... userIds);

    /**
     * 校验用户是否唯一
     *
     * @param user 筛选条件
     */
    boolean checkUserUnique(SysUser user, Long userId);

    /**
     * 校验相关参数的租户ID是否一致
     *
     * @param sysUserDTO 用户参数
     */
    void checkTenantId(SysUserDTO sysUserDTO);
}
