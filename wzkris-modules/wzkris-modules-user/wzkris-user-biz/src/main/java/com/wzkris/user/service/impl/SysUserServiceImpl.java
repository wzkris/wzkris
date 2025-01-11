package com.wzkris.user.service.impl;

import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.core.constant.SecurityConstants;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.orm.utils.DynamicTenantUtil;
import com.wzkris.user.domain.SysUser;
import com.wzkris.user.domain.SysUserPost;
import com.wzkris.user.domain.SysUserRole;
import com.wzkris.user.mapper.SysUserMapper;
import com.wzkris.user.mapper.SysUserPostMapper;
import com.wzkris.user.mapper.SysUserRoleMapper;
import com.wzkris.user.service.SysUserService;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 管理员 业务层处理
 *
 * @author wzkris
 */
@Service
@RequiredArgsConstructor
public class SysUserServiceImpl implements SysUserService {

    private final SysUserMapper userMapper;

    private final SysUserRoleMapper userRoleMapper;

    private final SysUserPostMapper userPostMapper;

    private final PasswordEncoder passwordEncoder;

    @Override
    public List<SysUser> list(SysUser user) {
        LambdaQueryWrapper<SysUser> lqw = this.buildQueryWrapper(user);
        return userMapper.selectListInScope(lqw);
    }

    @Override
    public List<SysUser> listAllocated(SysUser user, Long roleId) {
        List<Long> userIds = userRoleMapper.listUserIdByRoleId(roleId);
        if (CollectionUtils.isEmpty(userIds)) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<SysUser> lqw = this.buildQueryWrapper(user);
        lqw.in(SysUser::getUserId, userIds);
        return userMapper.selectListInScope(lqw);
    }

    @Override
    public List<SysUser> listUnallocated(SysUser user, Long roleId) {
        List<Long> userIds = userRoleMapper.listUserIdByRoleId(roleId);

        LambdaQueryWrapper<SysUser> lqw = this.buildQueryWrapper(user);
        if (!CollectionUtils.isEmpty(userIds)) {
            lqw.notIn(SysUser::getUserId, userIds);
        }
        return userMapper.selectListInScope(lqw);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insertUser(SysUser user, List<Long> roleIds, List<Long> postIds) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        boolean success = userMapper.insert(user) > 0;
        if (success) {
            // 新增用户与角色管理
            this.insertUserRole(user.getUserId(), roleIds);
            this.insertUserPost(user.getUserId(), postIds);
        }
        return success;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUser(SysUser user, List<Long> roleIds, List<Long> postIds) {
        boolean success = userMapper.updateById(user) > 0;
        if (success) {
            // 删除用户与角色关联
            userRoleMapper.deleteByUserId(user.getUserId());
            userPostMapper.deleteByUserId(user.getUserId());
            // 新增用户与角色管理
            this.insertUserRole(user.getUserId(), roleIds);
            this.insertUserPost(user.getUserId(), postIds);
        }
        return success;
    }

    @Override
    public void deleteByIds(List<Long> userIds) {
        userMapper.deleteByIds(userIds);
        userRoleMapper.deleteByUserIds(userIds);
        userPostMapper.deleteByUserIds(userIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void allocateRoles(Long userId, List<Long> roleIds) {
        userRoleMapper.deleteByUserId(userId);
        this.insertUserRole(userId, roleIds);
    }

    private void insertUserRole(Long userId, List<Long> roleIds) {
        if (ObjUtil.isNotEmpty(roleIds)) {
            List<SysUserRole> list = roleIds.stream()
                    .map(roleId -> new SysUserRole(userId, roleId))
                    .toList();
            userRoleMapper.insertBatch(list);
        }
    }

    private void insertUserPost(Long userId, List<Long> postIds) {
        if (ObjUtil.isNotEmpty(postIds)) {
            List<SysUserPost> list = postIds.stream()
                    .map(postId -> new SysUserPost(userId, postId))
                    .toList();
            userPostMapper.insertBatch(list);
        }
    }

    private LambdaQueryWrapper<SysUser> buildQueryWrapper(SysUser user) {
        return new LambdaQueryWrapper<SysUser>()
                .eq(StringUtil.isNotNull(user.getTenantId()), SysUser::getTenantId, user.getTenantId())
                .like(StringUtil.isNotNull(user.getUsername()), SysUser::getUsername, user.getUsername())
                .like(StringUtil.isNotNull(user.getNickname()), SysUser::getNickname, user.getNickname())
                .like(StringUtil.isNotNull(user.getPhoneNumber()), SysUser::getPhoneNumber, user.getPhoneNumber())
                .like(StringUtil.isNotNull(user.getEmail()), SysUser::getEmail, user.getEmail())
                .eq(StringUtil.isNotNull(user.getGender()), SysUser::getGender, user.getGender())
                .eq(StringUtil.isNotNull(user.getStatus()), SysUser::getStatus, user.getStatus())
                .eq(StringUtil.isNotNull(user.getStatus()), SysUser::getStatus, user.getStatus())
                .eq(StringUtil.isNotNull(user.getDeptId()), SysUser::getDeptId, user.getDeptId())
                .orderByDesc(SysUser::getUserId);
    }

    @Override
    public boolean checkUsedByUsername(@Nullable Long userId, @NotNull String username) {
        return DynamicTenantUtil.ignore(() -> {
            LambdaQueryWrapper<SysUser> lqw = new LambdaQueryWrapper<>(SysUser.class)
                    .eq(SysUser::getUsername, username)
                    .ne(ObjUtil.isNotNull(userId), SysUser::getUserId, userId);
            return userMapper.exists(lqw);
        });
    }

    @Override
    public boolean checkUsedByPhoneNumber(@org.jetbrains.annotations.Nullable Long userId, @NotNull String phonenumber) {
        return DynamicTenantUtil.ignore(() -> {
            LambdaQueryWrapper<SysUser> lqw = new LambdaQueryWrapper<>(SysUser.class)
                    .eq(SysUser::getPhoneNumber, phonenumber)
                    .ne(ObjUtil.isNotNull(userId), SysUser::getUserId, userId);
            return userMapper.exists(lqw);
        });
    }

    @Override
    public void checkDataScopes(List<Long> userIds) {
        userIds = userIds.stream().filter(Objects::nonNull).toList();
        if (ObjUtil.isNotEmpty(userIds)) {
            if (userIds.contains(SecurityConstants.SUPER_ADMIN_ID)) {
                throw new AccessDeniedException("无法访问超级管理员数据");
            }
            if (userMapper.checkDataScopes(userIds) != userIds.size()) {
                throw new AccessDeniedException("没有用户数据访问权限");
            }
        }
    }

}
