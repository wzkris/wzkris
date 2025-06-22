package com.wzkris.user.service;

import com.wzkris.user.domain.SysUser;
import com.wzkris.user.domain.req.SysUserQueryReq;
import com.wzkris.user.domain.vo.SelectVO;
import jakarta.annotation.Nullable;
import java.util.Collection;
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
     * @param queryReq 筛选条件
     */
    List<SysUser> list(SysUserQueryReq queryReq);

    /**
     * 根据条件分页查询已分配用户角色列表
     *
     * @param queryReq 筛选条件
     * @param roleId   角色ID
     * @return 系统用户选择列表
     */
    List<SelectVO> listAllocated(SysUserQueryReq queryReq, Long roleId);

    /**
     * 根据条件分页查询未分配用户角色列表
     *
     * @param queryReq 筛选条件
     * @param roleId   角色ID
     * @return 系统用户选择列表
     */
    List<SelectVO> listUnallocated(SysUserQueryReq queryReq, Long roleId);

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
     * 批量授权角色
     *
     * @param userId  管理员ID
     * @param roleIds 角色组
     */
    boolean allocateRoles(Long userId, List<Long> roleIds);

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

    /**
     * 校验是否有数据权限
     *
     * @param userIds 被操作的对象id
     */
    void checkDataScopes(Collection<Long> userIds);

    /**
     * 校验是否有数据权限
     *
     * @param userId 被操作的对象id
     */
    default void checkDataScopes(Long userId) {
        if (userId != null) {
            this.checkDataScopes(Collections.singleton(userId));
        }
    }
}
