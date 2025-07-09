package com.wzkris.user.service;

import com.wzkris.user.domain.SysUser;
import jakarta.annotation.Nullable;

import java.util.List;

/**
 * 管理员 业务层
 *
 * @author wzkris
 */
public interface SysUserService {

    /**
     * 新增管理员信息
     *
     * @param user    管理员信息
     * @param roleIds 关联角色
     * @param postIds 关联岗位
     */
    boolean insertUser(SysUser user, @Nullable List<Long> roleIds, @Nullable List<Long> postIds);

    /**
     * 修改管理员信息
     *
     * @param user    管理员信息
     * @param roleIds 关联角色
     * @param postIds 关联岗位
     */
    boolean updateUser(SysUser user, @Nullable List<Long> roleIds, @Nullable List<Long> postIds);

    /**
     * 硬删除用户
     *
     * @param userIds 用户ID
     */
    boolean deleteByIds(List<Long> userIds);

    /**
     * 修改用户角色
     *
     * @param userId  用户ID
     * @param roleIds 角色组
     */
    boolean updateGrantRoles(Long userId, List<Long> roleIds);

    /**
     * 校验用户名是否被使用
     *
     * @param userId   用户ID
     * @param username 用户名
     */
    boolean checkExistByUsername(@Nullable Long userId, String username);

    /**
     * 校验手机号是否被使用
     *
     * @param userId      用户ID
     * @param phonenumber 手机号
     */
    boolean checkExistByPhoneNumber(@Nullable Long userId, String phonenumber);

}
