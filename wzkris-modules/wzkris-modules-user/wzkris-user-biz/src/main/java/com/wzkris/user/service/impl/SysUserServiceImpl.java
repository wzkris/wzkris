package com.wzkris.user.service.impl;

import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.core.constant.SecurityConstants;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.orm.utils.DynamicTenantUtil;
import com.wzkris.common.security.oauth2.service.PasswordEncoderDelegate;
import com.wzkris.common.security.utils.LoginUtil;
import com.wzkris.user.domain.SysUser;
import com.wzkris.user.domain.SysUserPost;
import com.wzkris.user.domain.SysUserRole;
import com.wzkris.user.domain.req.SysUserQueryReq;
import com.wzkris.user.domain.vo.SelectVO;
import com.wzkris.user.mapper.SysUserMapper;
import com.wzkris.user.mapper.SysUserPostMapper;
import com.wzkris.user.mapper.SysUserRoleMapper;
import com.wzkris.user.service.SysUserService;
import jakarta.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

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
    public List<SysUser> list(SysUserQueryReq queryReq) {
        LambdaQueryWrapper<SysUser> lqw = this.buildQueryWrapper(queryReq);
        return userMapper.selectLists(lqw);
    }

    @Override
    public List<SelectVO> listAllocated(SysUserQueryReq queryReq, Long roleId) {
        List<Long> userIds = userRoleMapper.listUserIdByRoleId(roleId);
        if (CollectionUtils.isEmpty(userIds)) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<SysUser> lqw = this.buildQueryWrapper(queryReq);
        lqw.in(SysUser::getUserId, userIds);
        return userMapper.selectLists(lqw).stream().map(SelectVO::new).toList();
    }

    @Override
    public List<SelectVO> listUnallocated(SysUserQueryReq queryReq, Long roleId) {
        List<Long> userIds = userRoleMapper.listUserIdByRoleId(roleId);

        LambdaQueryWrapper<SysUser> lqw = this.buildQueryWrapper(queryReq);
        if (!CollectionUtils.isEmpty(userIds)) {
            lqw.notIn(SysUser::getUserId, userIds);
        }
        return userMapper.selectLists(lqw).stream().map(SelectVO::new).toList();
    }

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
        if (ObjUtil.isNotEmpty(roleIds)) {
            List<SysUserRole> list = roleIds.stream()
                    .map(roleId -> new SysUserRole(userId, roleId))
                    .toList();
            return userRoleMapper.insertBatch(list) > 0;
        }
        return false;
    }

    private void insertUserPost(Long userId, List<Long> postIds) {
        if (ObjUtil.isNotEmpty(postIds)) {
            List<SysUserPost> list = postIds.stream()
                    .map(postId -> new SysUserPost(userId, postId))
                    .toList();
            userPostMapper.insertBatch(list);
        }
    }

    private LambdaQueryWrapper<SysUser> buildQueryWrapper(SysUserQueryReq queryReq) {
        return new LambdaQueryWrapper<SysUser>()
                .eq(StringUtil.isNotNull(queryReq.getTenantId()), SysUser::getTenantId, queryReq.getTenantId())
                .like(StringUtil.isNotNull(queryReq.getUsername()), SysUser::getUsername, queryReq.getUsername())
                .like(StringUtil.isNotNull(queryReq.getNickname()), SysUser::getNickname, queryReq.getNickname())
                .like(
                        StringUtil.isNotNull(queryReq.getPhoneNumber()),
                        SysUser::getPhoneNumber,
                        queryReq.getPhoneNumber())
                .like(StringUtil.isNotNull(queryReq.getEmail()), SysUser::getEmail, queryReq.getEmail())
                .eq(StringUtil.isNotNull(queryReq.getStatus()), SysUser::getStatus, queryReq.getStatus())
                .eq(StringUtil.isNotNull(queryReq.getStatus()), SysUser::getStatus, queryReq.getStatus())
                .eq(StringUtil.isNotNull(queryReq.getDeptId()), SysUser::getDeptId, queryReq.getDeptId())
                .orderByDesc(SysUser::getUserId);
    }

    @Override
    public boolean checkExistByUsername(@Nullable Long userId, String username) {
        return DynamicTenantUtil.ignore(() -> {
            LambdaQueryWrapper<SysUser> lqw = new LambdaQueryWrapper<>(SysUser.class)
                    .eq(SysUser::getUsername, username)
                    .ne(ObjUtil.isNotNull(userId), SysUser::getUserId, userId);
            return userMapper.exists(lqw);
        });
    }

    @Override
    public boolean checkExistByPhoneNumber(@Nullable Long userId, String phonenumber) {
        return DynamicTenantUtil.ignore(() -> {
            LambdaQueryWrapper<SysUser> lqw = new LambdaQueryWrapper<>(SysUser.class)
                    .eq(SysUser::getPhoneNumber, phonenumber)
                    .ne(ObjUtil.isNotNull(userId), SysUser::getUserId, userId);
            return userMapper.exists(lqw);
        });
    }

    @Override
    public void checkDataScopes(Collection<Long> userIds) {
        if (ObjUtil.isNotEmpty(userIds)) {
            if (userIds.contains(SecurityConstants.SUPER_ADMIN_ID)) {
                throw new AccessDeniedException("禁止访问超级管理员数据");
            }
            if (userIds.contains(LoginUtil.getUserId())) {
                throw new AccessDeniedException("userId：‘" + LoginUtil.getUserId() + "'禁止访问自身数据");
            }
            if (!userMapper.checkDataScopes(userIds)) {
                throw new AccessDeniedException("无此用户数据访问权限");
            }
        }
    }
}
