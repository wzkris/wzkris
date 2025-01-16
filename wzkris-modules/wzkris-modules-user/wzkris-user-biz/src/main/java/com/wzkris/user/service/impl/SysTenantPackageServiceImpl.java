package com.wzkris.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wzkris.common.orm.utils.DynamicTenantUtil;
import com.wzkris.user.domain.SysTenant;
import com.wzkris.user.mapper.SysTenantMapper;
import com.wzkris.user.mapper.SysTenantPackageMapper;
import com.wzkris.user.service.SysTenantPackageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SysTenantPackageServiceImpl implements SysTenantPackageService {

    private final SysTenantMapper tenantMapper;

    private final SysTenantPackageMapper tenantPackageMapper;

    @Override
    public boolean checkPackageUsed(List<Long> packageIds) {
        return DynamicTenantUtil.ignore(() -> {
            LambdaQueryWrapper<SysTenant> lqw = Wrappers.lambdaQuery(SysTenant.class)
                    .in(SysTenant::getPackageId, packageIds);
            return tenantMapper.exists(lqw);
        });
    }

}
