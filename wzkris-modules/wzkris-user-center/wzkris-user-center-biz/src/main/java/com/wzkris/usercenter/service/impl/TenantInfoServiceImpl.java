package com.wzkris.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wzkris.common.orm.plus.ServiceImplPlus;
import com.wzkris.common.orm.utils.SkipTenantInterceptorUtil;
import com.wzkris.common.security.component.PasswordEncoderDelegate;
import com.wzkris.usercenter.domain.*;
import com.wzkris.usercenter.mapper.*;
import com.wzkris.usercenter.service.MemberInfoService;
import com.wzkris.usercenter.service.PostInfoService;
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

    private final MemberInfoMapper memberInfoMapper;

    private final MemberInfoService memberInfoService;

    private final PostInfoMapper postInfoMapper;

    private final PostInfoService postInfoService;

    private final TenantPackageInfoMapper tenantPackageInfoMapper;

    private final PasswordEncoderDelegate passwordEncoder;

    private final TenantWalletInfoMapper tenantWalletInfoMapper;

    private final TenantWalletRecordMapper tenantWalletRecordMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveTenant(TenantInfoDO tenant, String username, String password) {
        if (!passwordEncoder.isEncode(tenant.getOperPwd())) {
            tenant.setOperPwd(passwordEncoder.encode(tenant.getOperPwd()));
        }
        long memberId = IdWorker.getId();
        tenant.setAdministrator(memberId);
        baseMapper.insert(tenant);

        TenantWalletInfoDO wallet = new TenantWalletInfoDO(tenant.getId());
        tenantWalletInfoMapper.insert(wallet);

        MemberInfoDO memberInfoDO = new MemberInfoDO();
        memberInfoDO.setId(memberId);
        memberInfoDO.setTenantId(tenant.getId());
        memberInfoDO.setUsername(username);
        memberInfoDO.setPassword(password);
        return memberInfoService.saveMember(memberInfoDO, null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeTenant(Long tenantId) {
        return SkipTenantInterceptorUtil.ignore(() -> {
            boolean success = baseMapper.deleteById(tenantId) > 0;
            if (success) {
                tenantWalletInfoMapper.deleteById(tenantId);
                LambdaQueryWrapper<TenantWalletRecordDO> recordw = Wrappers.lambdaQuery(TenantWalletRecordDO.class)
                        .eq(TenantWalletRecordDO::getTenantId, tenantId);
                tenantWalletRecordMapper.delete(recordw);

                LambdaQueryWrapper<MemberInfoDO> userw = Wrappers.lambdaQuery(MemberInfoDO.class)
                        .select(MemberInfoDO::getId)
                        .eq(MemberInfoDO::getTenantId, tenantId);
                List<Long> memberIds = memberInfoMapper.selectList(userw).stream()
                        .map(MemberInfoDO::getId)
                        .toList();
                if (CollectionUtils.isNotEmpty(memberIds)) {
                    memberInfoService.removeMembers(memberIds);
                }
                LambdaQueryWrapper<PostInfoDO> rolew = Wrappers.lambdaQuery(PostInfoDO.class)
                        .select(PostInfoDO::getId)
                        .eq(PostInfoDO::getTenantId, tenantId);
                List<Long> postIds = postInfoMapper.selectList(rolew).stream()
                        .map(PostInfoDO::getId)
                        .toList();
                if (CollectionUtils.isNotEmpty(postIds)) {
                    postInfoService.removePosts(postIds);
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
            if (tenantPackage.getMemberNumLimit() == -1) {
                return true;
            }
            Long count = memberInfoMapper.selectCount(
                    Wrappers.lambdaQuery(MemberInfoDO.class).eq(MemberInfoDO::getTenantId, tenantId));
            return tenantPackage.getMemberNumLimit() - count > 0;
        });
    }

    @Override
    public boolean checkPostLimit(Long tenantId) {
        return SkipTenantInterceptorUtil.ignore(() -> {
            TenantInfoDO tenant = baseMapper.selectById(tenantId);
            TenantPackageInfoDO tenantPackage = tenantPackageInfoMapper.selectById(tenant.getPackageId());
            if (tenantPackage.getPostNumLimit() == -1) {
                return true;
            }
            Long count = postInfoMapper.selectCount(
                    Wrappers.lambdaQuery(PostInfoDO.class).eq(PostInfoDO::getTenantId, tenantId));
            return tenantPackage.getPostNumLimit() - count > 0;
        });
    }

    @Override
    public boolean checkAdministrator(List<Long> memberIds) {
        return SkipTenantInterceptorUtil.ignore(() -> {
            LambdaQueryWrapper<TenantInfoDO> lqw =
                    Wrappers.lambdaQuery(TenantInfoDO.class).in(TenantInfoDO::getAdministrator, memberIds);
            return baseMapper.exists(lqw);
        });
    }

}

