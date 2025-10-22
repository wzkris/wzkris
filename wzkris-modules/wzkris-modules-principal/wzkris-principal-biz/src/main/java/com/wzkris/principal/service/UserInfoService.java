package com.wzkris.principal.service;

import com.wzkris.principal.domain.UserInfoDO;
import jakarta.annotation.Nullable;

import java.util.List;

/**
 * 管理员 业务层
 *
 * @author wzkris
 */
public interface UserInfoService {

    /**
     * 新增管理员信息
     *
     * @param user    管理员信息
     * @param roleIds 关联角色
     */
    boolean saveUser(UserInfoDO user, @Nullable List<Long> roleIds);

    /**
     * 修改管理员信息
     *
     * @param user    管理员信息
     * @param roleIds 关联角色
     */
    boolean modifyUser(UserInfoDO user, @Nullable List<Long> roleIds);

    /**
     * 删除用户
     *
     * @param userIds 用户ID
     */
    boolean removeByIds(List<Long> userIds);

    /**
     * 用户分配角色
     *
     * @param userId  用户ID
     * @param roleIds 角色组
     */
    boolean grantRoles(Long userId, List<Long> roleIds);

    /**
     * 校验用户名是否被使用
     *
     * @param userId   用户ID
     * @param username 用户名
     */
    boolean existByUsername(@Nullable Long userId, String username);

    /**
     * 校验手机号是否被使用
     *
     * @param userId      用户ID
     * @param phonenumber 手机号
     */
    boolean existByPhoneNumber(@Nullable Long userId, String phonenumber);

}
