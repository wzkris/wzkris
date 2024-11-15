package com.wzkris.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.user.domain.SysTenantPackage;
import com.wzkris.user.mapper.SysTenantPackageMapper;
import com.wzkris.user.service.SysTenantPackageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SysTenantPackageServiceImpl implements SysTenantPackageService {

    private final SysTenantPackageMapper tenantPackageMapper;

    @Override
    public List<SysTenantPackage> list(SysTenantPackage sysTenantPackage) {
        LambdaQueryWrapper<SysTenantPackage> lqw = this.buildQueryWrapper(sysTenantPackage);
        return tenantPackageMapper.selectList(lqw);
    }

    private LambdaQueryWrapper<SysTenantPackage> buildQueryWrapper(SysTenantPackage sysTenantPackage) {
        return new LambdaQueryWrapper<SysTenantPackage>()
                .like(StringUtil.isNotNull(sysTenantPackage.getPackageName()), SysTenantPackage::getPackageName, sysTenantPackage.getPackageName())
                .eq(StringUtil.isNotNull(sysTenantPackage.getStatus()), SysTenantPackage::getStatus, sysTenantPackage.getStatus())
                .orderByDesc(SysTenantPackage::getPackageId);
    }
}
