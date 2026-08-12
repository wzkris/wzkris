package com.wzkris.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wzkris.common.orm.plus.ServiceImplPlus;
import com.wzkris.common.orm.utils.SkipTenantInterceptorUtil;
import com.wzkris.common.security.component.PasswordEncoderDelegate;
import com.wzkris.usercenter.domain.*;
import com.wzkris.usercenter.mapper.*;
import com.wzkris.usercenter.service.TenantUserService;
import com.wzkris.usercenter.service.TenantRoleService;
import com.wzkris.usercenter.service.TenantInfoService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 租户层
 *
 * @author wzkris
 */
@Service
@RequiredArgsConstructor
public class TenantInfoServiceImpl
        extends ServiceImplPlus<TenantInfoMapper, TenantInfoDO>
        implements TenantInfoService {

    private final TenantUserMapper tenantUserMapper;

    private final TenantUserService tenantUserService;

    private final TenantRoleMapper tenantRoleMapper;

    private final TenantRoleService tenantRoleService;

    private final TenantPackageInfoMapper tenantPackageInfoMapper;

    private final PasswordEncoderDelegate passwordEncoder;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveTenant(TenantInfoDO tenant, String username, String password) {
        if (!passwordEncoder.isEncode(tenant.getOperPwd())) {
            tenant.setOperPwd(passwordEncoder.encode(tenant.getOperPwd()));
        }
        long tenantUserId = IdWorker.getId();
        tenant.setAdministrator(tenantUserId);
        baseMapper.insert(tenant);

        TenantUserDO tenantUserDO = new TenantUserDO();
        tenantUserDO.setId(tenantUserId);
        tenantUserDO.setTenantId(tenant.getId());
        tenantUserDO.setUsername(username);
        tenantUserDO.setPassword(password);
        return tenantUserService.saveTenantUser(tenantUserDO, null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeTenant(Long tenantId) {
        return SkipTenantInterceptorUtil.ignore(() -> {
            boolean success = baseMapper.deleteById(tenantId) > 0;
            if (success) {
                LambdaQueryWrapper<TenantUserDO> userw = Wrappers.lambdaQuery(TenantUserDO.class)
                        .select(TenantUserDO::getId)
                        .eq(TenantUserDO::getTenantId, tenantId);
                List<Long> tenantUserIds = tenantUserMapper.selectList(userw).stream()
                        .map(TenantUserDO::getId)
                        .toList();
                if (CollectionUtils.isNotEmpty(tenantUserIds)) {
                    tenantUserService.removeTenantUsers(tenantUserIds);
                }
                LambdaQueryWrapper<TenantRoleDO> rolew = Wrappers.lambdaQuery(TenantRoleDO.class)
                        .select(TenantRoleDO::getId)
                        .eq(TenantRoleDO::getTenantId, tenantId);
                List<Long> tenantRoleIds = tenantRoleMapper.selectList(rolew).stream()
                        .map(TenantRoleDO::getId)
                        .toList();
                if (CollectionUtils.isNotEmpty(tenantRoleIds)) {
                    tenantRoleService.removeRoles(tenantRoleIds);
                }
            }
            return success;
        });
    }

    @Override
    public boolean checkAccountLimit(Long tenantId) {
        return SkipTenantInterceptorUtil.ignore(() -> {
            TenantInfoDO tenant = baseMapper.selectById(tenantId);
            TenantPackageInfoDO tenantPackage = tenantPackageInfoMapper.selectById(tenant.getPackageId());
            if (tenantPackage.getAccountNumLimit() == -1) {
                return true;
            }
            Long count = tenantUserMapper.selectCount(
                    Wrappers.lambdaQuery(TenantUserDO.class).eq(TenantUserDO::getTenantId, tenantId));
            return tenantPackage.getAccountNumLimit() - count > 0;
        });
    }

    @Override
    public boolean checkRoleLimit(Long tenantId) {
        return SkipTenantInterceptorUtil.ignore(() -> {
            TenantInfoDO tenant = baseMapper.selectById(tenantId);
            TenantPackageInfoDO tenantPackage = tenantPackageInfoMapper.selectById(tenant.getPackageId());
            if (tenantPackage.getRoleNumLimit() == -1) {
                return true;
            }
            Long count = tenantRoleMapper.selectCount(
                    Wrappers.lambdaQuery(TenantRoleDO.class).eq(TenantRoleDO::getTenantId, tenantId));
            return tenantPackage.getRoleNumLimit() - count > 0;
        });
    }

    @Override
    public boolean checkAdministrator(List<Long> tenantUserIds) {
        return SkipTenantInterceptorUtil.ignore(() -> {
            LambdaQueryWrapper<TenantInfoDO> lqw =
                    Wrappers.lambdaQuery(TenantInfoDO.class).in(TenantInfoDO::getAdministrator, tenantUserIds);
            return baseMapper.exists(lqw);
        });
    }

}

