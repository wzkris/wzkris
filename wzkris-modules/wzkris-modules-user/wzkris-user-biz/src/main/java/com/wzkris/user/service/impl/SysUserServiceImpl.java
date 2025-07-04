package com.wzkris.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.orm.utils.DynamicTenantUtil;
import com.wzkris.common.security.oauth2.service.PasswordEncoderDelegate;
import com.wzkris.user.domain.SysUser;
import com.wzkris.user.domain.SysUserPost;
import com.wzkris.user.domain.SysUserRole;
import com.wzkris.user.domain.req.SysUserQueryReq;
import com.wzkris.user.mapper.SysUserMapper;
import com.wzkris.user.mapper.SysUserPostMapper;
import com.wzkris.user.mapper.SysUserRoleMapper;
import com.wzkris.user.service.SysUserService;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    private final PasswordEncoderDelegate passwordEncoder;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insertUser(SysUser user, List<Long> roleIds, List<Long> postIds) {
        if (user.getPassword() != null && !passwordEncoder.isEncode(user.getPassword())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
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
        if (user.getPassword() != null && !passwordEncoder.isEncode(user.getPassword())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
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
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteByIds(List<Long> userIds) {
        boolean success = userMapper.deleteByIds(userIds) > 0;
        if (success) {
            userRoleMapper.deleteByUserIds(userIds);
            userPostMapper.deleteByUserIds(userIds);
        }
        return success;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean allocateRoles(Long userId, List<Long> roleIds) {
        userRoleMapper.deleteByUserId(userId);
        return this.insertUserRole(userId, roleIds);
    }

    private boolean insertUserRole(Long userId, List<Long> roleIds) {
        if (CollectionUtils.isNotEmpty(roleIds)) {
            List<SysUserRole> list = roleIds.stream()
                    .map(roleId -> new SysUserRole(userId, roleId))
                    .toList();
            return userRoleMapper.insertBatch(list) > 0;
        }
        return false;
    }

    private void insertUserPost(Long userId, List<Long> postIds) {
        if (CollectionUtils.isNotEmpty(postIds)) {
            List<SysUserPost> list = postIds.stream()
                    .map(postId -> new SysUserPost(userId, postId))
                    .toList();
            userPostMapper.insertBatch(list);
        }
    }

    private LambdaQueryWrapper<SysUser> buildQueryWrapper(SysUserQueryReq queryReq) {
        return new LambdaQueryWrapper<SysUser>()
                .eq(ObjectUtils.isNotEmpty(queryReq.getTenantId()), SysUser::getTenantId, queryReq.getTenantId())
                .like(ObjectUtils.isNotEmpty(queryReq.getUsername()), SysUser::getUsername, queryReq.getUsername())
                .like(ObjectUtils.isNotEmpty(queryReq.getNickname()), SysUser::getNickname, queryReq.getNickname())
                .like(
                        ObjectUtils.isNotEmpty(queryReq.getPhoneNumber()),
                        SysUser::getPhoneNumber,
                        queryReq.getPhoneNumber())
                .like(ObjectUtils.isNotEmpty(queryReq.getEmail()), SysUser::getEmail, queryReq.getEmail())
                .eq(ObjectUtils.isNotEmpty(queryReq.getStatus()), SysUser::getStatus, queryReq.getStatus())
                .eq(ObjectUtils.isNotEmpty(queryReq.getStatus()), SysUser::getStatus, queryReq.getStatus())
                .eq(ObjectUtils.isNotEmpty(queryReq.getDeptId()), SysUser::getDeptId, queryReq.getDeptId())
                .orderByDesc(SysUser::getUserId);
    }

    @Override
    public boolean checkExistByUsername(@Nullable Long userId, String username) {
        return DynamicTenantUtil.ignore(() -> {
            LambdaQueryWrapper<SysUser> lqw = new LambdaQueryWrapper<>(SysUser.class)
                    .eq(SysUser::getUsername, username)
                    .ne(Objects.nonNull(userId), SysUser::getUserId, userId);
            return userMapper.exists(lqw);
        });
    }

    @Override
    public boolean checkExistByPhoneNumber(@Nullable Long userId, String phonenumber) {
        return DynamicTenantUtil.ignore(() -> {
            LambdaQueryWrapper<SysUser> lqw = new LambdaQueryWrapper<>(SysUser.class)
                    .eq(SysUser::getPhoneNumber, phonenumber)
                    .ne(Objects.nonNull(userId), SysUser::getUserId, userId);
            return userMapper.exists(lqw);
        });
    }

}
