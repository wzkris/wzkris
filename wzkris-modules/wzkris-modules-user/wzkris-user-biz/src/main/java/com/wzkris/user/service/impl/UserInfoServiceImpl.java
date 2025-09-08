package com.wzkris.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.orm.utils.DynamicTenantUtil;
import com.wzkris.common.security.oauth2.service.PasswordEncoderDelegate;
import com.wzkris.user.domain.UserInfoDO;
import com.wzkris.user.domain.UserToRoleDO;
import com.wzkris.user.mapper.UserInfoMapper;
import com.wzkris.user.mapper.UserToRoleMapper;
import com.wzkris.user.service.UserInfoService;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
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
public class UserInfoServiceImpl implements UserInfoService {

    private final UserInfoMapper userInfoMapper;

    private final UserToRoleMapper userToRoleMapper;

    private final PasswordEncoderDelegate passwordEncoder;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveUser(UserInfoDO user, List<Long> roleIds) {
        if (user.getPassword() != null && !passwordEncoder.isEncode(user.getPassword())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        boolean success = userInfoMapper.insert(user) > 0;
        if (success) {
            this.insertUserRole(user.getUserId(), roleIds);
        }
        return success;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean modifyUser(UserInfoDO user, List<Long> roleIds) {
        if (user.getPassword() != null && !passwordEncoder.isEncode(user.getPassword())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        boolean success = userInfoMapper.updateById(user) > 0;
        if (success && roleIds != null) {
            userToRoleMapper.deleteByUserId(user.getUserId());
            this.insertUserRole(user.getUserId(), roleIds);
        }
        return success;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeByIds(List<Long> userIds) {
        boolean success = userInfoMapper.deleteByIds(userIds) > 0;
        if (success) {
            userToRoleMapper.deleteByUserIds(userIds);
        }
        return success;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean grantRoles(Long userId, List<Long> roleIds) {
        userToRoleMapper.deleteByUserId(userId);
        return this.insertUserRole(userId, roleIds);
    }

    private boolean insertUserRole(Long userId, List<Long> roleIds) {
        if (CollectionUtils.isNotEmpty(roleIds)) {
            List<UserToRoleDO> list = roleIds.stream()
                    .map(roleId -> new UserToRoleDO(userId, roleId))
                    .toList();
            return userToRoleMapper.insert(list) > 0;
        }
        return false;
    }

    @Override
    public boolean existByUsername(@Nullable Long userId, String username) {
        return DynamicTenantUtil.ignore(() -> {
            LambdaQueryWrapper<UserInfoDO> lqw = new LambdaQueryWrapper<>(UserInfoDO.class)
                    .eq(UserInfoDO::getUsername, username)
                    .ne(Objects.nonNull(userId), UserInfoDO::getUserId, userId);
            return userInfoMapper.exists(lqw);
        });
    }

    @Override
    public boolean existByPhoneNumber(@Nullable Long userId, String phonenumber) {
        return DynamicTenantUtil.ignore(() -> {
            LambdaQueryWrapper<UserInfoDO> lqw = new LambdaQueryWrapper<>(UserInfoDO.class)
                    .eq(UserInfoDO::getPhoneNumber, phonenumber)
                    .ne(Objects.nonNull(userId), UserInfoDO::getUserId, userId);
            return userInfoMapper.exists(lqw);
        });
    }

}
