package com.wzkris.usercenter.service;

import com.wzkris.usercenter.domain.AdminInfoDO;
import jakarta.annotation.Nullable;

import java.util.List;

/**
 * 管理员 业务层
 *
 * @author wzkris
 */
public interface AdminInfoService {

    /**
     * 新增管理员信息
     *
     * @param admin   管理员信息
     * @param roleIds 关联角色
     */
    boolean saveAdmin(AdminInfoDO admin, @Nullable List<Long> roleIds);

    /**
     * 修改管理员信息
     *
     * @param admin   管理员信息
     * @param roleIds 关联角色
     */
    boolean modifyAdmin(AdminInfoDO admin, @Nullable List<Long> roleIds);

    /**
     * 删除用户
     *
     * @param adminIds 用户ID
     */
    boolean removeByIds(List<Long> adminIds);

    /**
     * 用户分配角色
     *
     * @param adminId 用户ID
     * @param roleIds 角色组
     */
    boolean grantRoles(Long adminId, List<Long> roleIds);

    /**
     * 校验用户名是否被使用
     *
     * @param adminId  用户ID
     * @param username 用户名
     */
    boolean existByUsername(@Nullable Long adminId, String username);

    /**
     * 校验手机号是否被使用
     *
     * @param adminId     用户ID
     * @param phonenumber 手机号
     */
    boolean existByPhoneNumber(@Nullable Long adminId, String phonenumber);

}
