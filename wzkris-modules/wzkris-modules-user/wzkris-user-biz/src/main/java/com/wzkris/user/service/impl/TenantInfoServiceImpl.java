package com.wzkris.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.orm.utils.DynamicTenantUtil;
import com.wzkris.common.security.oauth2.service.PasswordEncoderDelegate;
import com.wzkris.user.domain.*;
import com.wzkris.user.domain.vo.SelectVO;
import com.wzkris.user.mapper.*;
import com.wzkris.user.service.PostInfoService;
import com.wzkris.user.service.StaffInfoService;
import com.wzkris.user.service.TenantInfoService;
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

    private final StaffInfoMapper staffInfoMapper;

    private final StaffInfoService staffInfoService;

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
    public boolean saveTenant(TenantInfoDO tenant, String staffName, String password) {
        if (!passwordEncoder.isEncode(tenant.getOperPwd())) {
            tenant.setOperPwd(passwordEncoder.encode(tenant.getOperPwd()));
        }
        long staffId = IdWorker.getId();
        tenant.setAdministrator(staffId);
        tenantInfoMapper.insert(tenant);

        TenantWalletInfoDO wallet = new TenantWalletInfoDO(tenant.getTenantId());
        tenantWalletInfoMapper.insert(wallet);

        StaffInfoDO staff = new StaffInfoDO();
        staff.setStaffId(staffId);
        staff.setTenantId(tenant.getTenantId());
        staff.setStaffName(staffName);
        staff.setPassword(password);
        return staffInfoService.saveStaff(staff, null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeById(Long tenantId) {
        return DynamicTenantUtil.ignore(() -> {
            boolean success = tenantInfoMapper.deleteById(tenantId) > 0;
            if (success) {
                tenantWalletInfoMapper.deleteById(tenantId);
                LambdaQueryWrapper<TenantWalletRecordDO> recordw = Wrappers.lambdaQuery(TenantWalletRecordDO.class)
                        .eq(TenantWalletRecordDO::getTenantId, tenantId);
                tenantWalletRecordMapper.delete(recordw);

                LambdaQueryWrapper<StaffInfoDO> userw = Wrappers.lambdaQuery(StaffInfoDO.class)
                        .select(StaffInfoDO::getStaffId)
                        .eq(StaffInfoDO::getTenantId, tenantId);
                List<Long> staffIds = staffInfoMapper.selectList(userw).stream()
                        .map(StaffInfoDO::getStaffId)
                        .toList();
                if (CollectionUtils.isNotEmpty(staffIds)) {
                    staffInfoService.removeByIds(staffIds);
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
        return TenantInfoDO.isSuperTenant(tenantId)
                || DynamicTenantUtil.ignore(() -> {
            TenantInfoDO tenant = tenantInfoMapper.selectById(tenantId);
            if (tenant.getAccountLimit() == -1) {
                return true;
            }
            Long count = staffInfoMapper.selectCount(
                    Wrappers.lambdaQuery(StaffInfoDO.class).eq(StaffInfoDO::getTenantId, tenantId));
            return tenant.getAccountLimit() - count > 0;
        });
    }

    @Override
    public boolean checkRoleLimit(Long tenantId) {
        return TenantInfoDO.isSuperTenant(tenantId)
                || DynamicTenantUtil.ignore(() -> {
            TenantInfoDO tenant = tenantInfoMapper.selectById(tenantId);
            if (tenant.getRoleLimit() == -1) {
                return true;
            }
            Long count = postInfoMapper.selectCount(
                    Wrappers.lambdaQuery(PostInfoDO.class).eq(PostInfoDO::getTenantId, tenantId));
            return tenant.getRoleLimit() - count > 0;
        });
    }

    @Override
    public boolean checkAdministrator(List<Long> userIds) {
        return DynamicTenantUtil.ignore(() -> {
            LambdaQueryWrapper<TenantInfoDO> lqw =
                    Wrappers.lambdaQuery(TenantInfoDO.class).in(TenantInfoDO::getAdministrator, userIds);
            return tenantInfoMapper.exists(lqw);
        });
    }

    @Override
    public void checkDataScope(Long tenantId) {
        if (TenantInfoDO.isSuperTenant(tenantId)) {
            throw new AccessDeniedException("不允许访问超级租户数据");
        }
    }

}
