package com.wzkris.principal.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.orm.utils.SkipTenantInterceptorUtil;
import com.wzkris.common.security.component.PasswordEncoderDelegate;
import com.wzkris.principal.domain.*;
import com.wzkris.principal.domain.vo.SelectVO;
import com.wzkris.principal.mapper.*;
import com.wzkris.principal.service.MemberInfoService;
import com.wzkris.principal.service.PostInfoService;
import com.wzkris.principal.service.TenantInfoService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 租户层
 *
 * @author wzkris
 */
@Service
@RequiredArgsConstructor
public class TenantInfoServiceImpl implements TenantInfoService {

    private final MemberInfoMapper memberInfoMapper;

    private final MemberInfoService memberInfoService;

    private final PostInfoMapper postInfoMapper;

    private final PostInfoService postInfoService;

    private final PasswordEncoderDelegate passwordEncoder;

    private final TenantInfoMapper tenantInfoMapper;

    private final TenantWalletInfoMapper tenantWalletInfoMapper;

    private final TenantWalletRecordMapper tenantWalletRecordMapper;

    @Override
    public List<SelectVO> listSelect(String tenantName) {
        LambdaQueryWrapper<TenantInfoDO> lqw = new LambdaQueryWrapper<TenantInfoDO>()
                .select(TenantInfoDO::getTenantId, TenantInfoDO::getTenantName)
                .like(StringUtil.isNotBlank(tenantName), TenantInfoDO::getTenantName, tenantName)
                .orderByAsc(TenantInfoDO::getTenantId);
        return tenantInfoMapper.selectList(lqw).stream().map(SelectVO::new).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveTenant(TenantInfoDO tenant, String username, String password) {
        if (!passwordEncoder.isEncode(tenant.getOperPwd())) {
            tenant.setOperPwd(passwordEncoder.encode(tenant.getOperPwd()));
        }
        long memberId = IdWorker.getId();
        tenant.setAdministrator(memberId);
        tenantInfoMapper.insert(tenant);

        TenantWalletInfoDO wallet = new TenantWalletInfoDO(tenant.getTenantId());
        tenantWalletInfoMapper.insert(wallet);

        MemberInfoDO memberInfoDO = new MemberInfoDO();
        memberInfoDO.setMemberId(memberId);
        memberInfoDO.setTenantId(tenant.getTenantId());
        memberInfoDO.setUsername(username);
        memberInfoDO.setPassword(password);
        return memberInfoService.saveMember(memberInfoDO, null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeById(Long tenantId) {
        return SkipTenantInterceptorUtil.ignore(() -> {
            boolean success = tenantInfoMapper.deleteById(tenantId) > 0;
            if (success) {
                tenantWalletInfoMapper.deleteById(tenantId);
                LambdaQueryWrapper<TenantWalletRecordDO> recordw = Wrappers.lambdaQuery(TenantWalletRecordDO.class)
                        .eq(TenantWalletRecordDO::getTenantId, tenantId);
                tenantWalletRecordMapper.delete(recordw);

                LambdaQueryWrapper<MemberInfoDO> userw = Wrappers.lambdaQuery(MemberInfoDO.class)
                        .select(MemberInfoDO::getMemberId)
                        .eq(MemberInfoDO::getTenantId, tenantId);
                List<Long> memberIds = memberInfoMapper.selectList(userw).stream()
                        .map(MemberInfoDO::getMemberId)
                        .toList();
                if (CollectionUtils.isNotEmpty(memberIds)) {
                    memberInfoService.removeByIds(memberIds);
                }
                LambdaQueryWrapper<PostInfoDO> rolew = Wrappers.lambdaQuery(PostInfoDO.class)
                        .select(PostInfoDO::getPostId)
                        .eq(PostInfoDO::getTenantId, tenantId);
                List<Long> postIds = postInfoMapper.selectList(rolew).stream()
                        .map(PostInfoDO::getPostId)
                        .toList();
                if (CollectionUtils.isNotEmpty(postIds)) {
                    postInfoService.removeByIds(postIds);
                }
            }
            return success;
        });
    }

    @Override
    public boolean checkAccountLimit(Long tenantId) {
        return SkipTenantInterceptorUtil.ignore(() -> {
            TenantInfoDO tenant = tenantInfoMapper.selectById(tenantId);
            if (tenant.getAccountLimit() == -1) {
                return true;
            }
            Long count = memberInfoMapper.selectCount(
                    Wrappers.lambdaQuery(MemberInfoDO.class).eq(MemberInfoDO::getTenantId, tenantId));
            return tenant.getAccountLimit() - count > 0;
        });
    }

    @Override
    public boolean checkPostLimit(Long tenantId) {
        return SkipTenantInterceptorUtil.ignore(() -> {
            TenantInfoDO tenant = tenantInfoMapper.selectById(tenantId);
            if (tenant.getPostLimit() == -1) {
                return true;
            }
            Long count = postInfoMapper.selectCount(
                    Wrappers.lambdaQuery(PostInfoDO.class).eq(PostInfoDO::getTenantId, tenantId));
            return tenant.getPostLimit() - count > 0;
        });
    }

    @Override
    public void checkAdministrator(List<Long> memberIds) {
        SkipTenantInterceptorUtil.ignore(() -> {
            LambdaQueryWrapper<TenantInfoDO> lqw =
                    Wrappers.lambdaQuery(TenantInfoDO.class).in(TenantInfoDO::getAdministrator, memberIds);
            if (tenantInfoMapper.exists(lqw)) {
                throw new AccessDeniedException("禁止操作超级管理员");
            }
        });
    }

}
