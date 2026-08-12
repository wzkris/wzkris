package com.wzkris.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.orm.plus.ServiceImplPlus;
import com.wzkris.common.orm.utils.SkipTenantInterceptorUtil;
import com.wzkris.common.security.component.PasswordEncoderDelegate;
import com.wzkris.usercenter.domain.TenantUserDO;
import com.wzkris.usercenter.domain.TenantUserToRoleDO;
import com.wzkris.usercenter.mapper.TenantUserMapper;
import com.wzkris.usercenter.mapper.TenantUserToRoleMapper;
import com.wzkris.usercenter.service.TenantUserService;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TenantUserServiceImpl
        extends ServiceImplPlus<TenantUserMapper, TenantUserDO>
        implements TenantUserService {

    private final TenantUserToRoleMapper tenantUserToRoleMapper;

    private final PasswordEncoderDelegate passwordEncoder;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveTenantUser(TenantUserDO tenantUser, @Nullable List<Long> tenantRoleIds) {
        if (tenantUser.getPassword() != null && !passwordEncoder.isEncode(tenantUser.getPassword())) {
            tenantUser.setPassword(passwordEncoder.encode(tenantUser.getPassword()));
        }
        boolean success = baseMapper.insert(tenantUser) > 0;
        if (success) {
            this.insertTenantUserToRole(tenantUser.getId(), tenantRoleIds);
        }
        return success;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateTenantUser(TenantUserDO tenantUser, List<Long> tenantRoleIds) {
        if (tenantUser.getPassword() != null && !passwordEncoder.isEncode(tenantUser.getPassword())) {
            tenantUser.setPassword(passwordEncoder.encode(tenantUser.getPassword()));
        }
        boolean success = baseMapper.updateById(tenantUser) > 0;
        if (success && tenantRoleIds != null) {
            tenantUserToRoleMapper.delete(new LambdaQueryWrapper<>(TenantUserToRoleDO.class)
                    .eq(TenantUserToRoleDO::getTenantUserId, tenantUser.getId()));
            this.insertTenantUserToRole(tenantUser.getId(), tenantRoleIds);
        }
        return success;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean grantRoles(Long tenantUserId, List<Long> tenantRoleIds) {
        tenantUserToRoleMapper.delete(new LambdaQueryWrapper<>(TenantUserToRoleDO.class)
                .eq(TenantUserToRoleDO::getTenantUserId, tenantUserId));
        return this.insertTenantUserToRole(tenantUserId, tenantRoleIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeTenantUsers(List<Long> tenantUserIds) {
        boolean success = baseMapper.deleteByIds(tenantUserIds) > 0;
        if (success) {
            tenantUserToRoleMapper.delete(new LambdaQueryWrapper<>(TenantUserToRoleDO.class)
                    .in(TenantUserToRoleDO::getTenantUserId, tenantUserIds));
        }
        return success;
    }

    @Override
    public boolean existByUsername(Long tenantUserId, String username) {
        return SkipTenantInterceptorUtil.ignore(() -> {
            LambdaQueryWrapper<TenantUserDO> lqw = new LambdaQueryWrapper<>(TenantUserDO.class)
                    .eq(TenantUserDO::getUsername, username)
                    .ne(Objects.nonNull(tenantUserId), TenantUserDO::getId, tenantUserId);
            return baseMapper.exists(lqw);
        });
    }

    @Override
    public boolean existByPhoneNumber(Long tenantUserId, String phoneNumber) {
        return SkipTenantInterceptorUtil.ignore(() -> {
            LambdaQueryWrapper<TenantUserDO> lqw = new LambdaQueryWrapper<>(TenantUserDO.class)
                    .eq(TenantUserDO::getPhoneNumber, phoneNumber)
                    .ne(Objects.nonNull(tenantUserId), TenantUserDO::getId, tenantUserId);
            return baseMapper.exists(lqw);
        });
    }

    private boolean insertTenantUserToRole(Long tenantUserId, List<Long> tenantRoleIds) {
        if (CollectionUtils.isNotEmpty(tenantRoleIds)) {
            List<TenantUserToRoleDO> list = tenantRoleIds.stream()
                    .map(tenantRoleId -> new TenantUserToRoleDO(tenantUserId, tenantRoleId))
                    .toList();
            return !tenantUserToRoleMapper.insert(list).isEmpty();
        }
        return false;
    }

}
