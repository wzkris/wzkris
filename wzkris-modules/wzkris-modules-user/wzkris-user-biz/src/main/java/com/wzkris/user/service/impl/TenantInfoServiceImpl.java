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
import com.wzkris.user.service.RoleInfoService;
import com.wzkris.user.service.TenantInfoService;
import com.wzkris.user.service.UserInfoService;
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

    private final UserInfoMapper userInfoMapper;

    private final UserInfoService userInfoService;

    private final RoleInfoMapper roleInfoMapper;

    private final RoleInfoService roleInfoService;

    private final DeptInfoMapper deptInfoMapper;

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
        long userId = IdWorker.getId();
        tenant.setAdministrator(userId);
        tenantInfoMapper.insert(tenant);

        TenantWalletInfoDO wallet = new TenantWalletInfoDO(tenant.getTenantId());
        tenantWalletInfoMapper.insert(wallet);

        UserInfoDO user = new UserInfoDO();
        user.setUserId(userId);
        user.setTenantId(tenant.getTenantId());
        user.setUsername(username);
        user.setPassword(password);
        return userInfoService.saveUser(user, null);
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

                LambdaQueryWrapper<UserInfoDO> userw = Wrappers.lambdaQuery(UserInfoDO.class)
                        .select(UserInfoDO::getUserId)
                        .eq(UserInfoDO::getTenantId, tenantId);
                List<Long> userIds = userInfoMapper.selectList(userw).stream()
                        .map(UserInfoDO::getUserId)
                        .toList();
                if (CollectionUtils.isNotEmpty(userIds)) {
                    userInfoService.removeByIds(userIds);
                }
                LambdaQueryWrapper<RoleInfoDO> rolew = Wrappers.lambdaQuery(RoleInfoDO.class)
                        .select(RoleInfoDO::getRoleId)
                        .eq(RoleInfoDO::getTenantId, tenantId);
                List<Long> roleIds = roleInfoMapper.selectList(rolew).stream()
                        .map(RoleInfoDO::getRoleId)
                        .toList();
                if (CollectionUtils.isNotEmpty(roleIds)) {
                    roleInfoService.removeByIds(roleIds);
                }
                LambdaQueryWrapper<DeptInfoDO> deptw =
                        Wrappers.lambdaQuery(DeptInfoDO.class).eq(DeptInfoDO::getTenantId, tenantId);
                deptInfoMapper.delete(deptw);
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
            Long count = userInfoMapper.selectCount(
                    Wrappers.lambdaQuery(UserInfoDO.class).eq(UserInfoDO::getTenantId, tenantId));
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
            Long count = roleInfoMapper.selectCount(
                    Wrappers.lambdaQuery(RoleInfoDO.class).eq(RoleInfoDO::getTenantId, tenantId));
            return tenant.getRoleLimit() - count > 0;
        });
    }

    @Override
    public boolean checkDeptLimit(Long tenantId) {
        return TenantInfoDO.isSuperTenant(tenantId)
                || DynamicTenantUtil.ignore(() -> {
            TenantInfoDO tenant = tenantInfoMapper.selectById(tenantId);
            if (tenant.getDeptLimit() == -1) {
                return true;
            }
            Long count = deptInfoMapper.selectCount(
                    Wrappers.lambdaQuery(DeptInfoDO.class).eq(DeptInfoDO::getTenantId, tenantId));
            return tenant.getDeptLimit() - count > 0;
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
