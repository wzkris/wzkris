package com.wzkris.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wzkris.common.core.constant.CommonConstants;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.orm.utils.DynamicTenantUtil;
import com.wzkris.user.domain.SysTenant;
import com.wzkris.user.domain.SysTenantPackage;
import com.wzkris.user.domain.vo.SelectVO;
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
    public List<SelectVO> listSelect(String packageName) {
        LambdaQueryWrapper<SysTenantPackage> lqw = new LambdaQueryWrapper<SysTenantPackage>()
                .select(SysTenantPackage::getPackageId, SysTenantPackage::getPackageName)
                .eq(SysTenantPackage::getStatus, CommonConstants.STATUS_ENABLE)
                .like(StringUtil.isNotBlank(packageName), SysTenantPackage::getPackageName, packageName)
                .orderByAsc(SysTenantPackage::getPackageId);
        return tenantPackageMapper.selectList(lqw).stream().map(SelectVO::new).toList();
    }

    @Override
    public boolean checkPackageUsed(List<Long> packageIds) {
        return DynamicTenantUtil.ignore(() -> {
            LambdaQueryWrapper<SysTenant> lqw =
                    Wrappers.lambdaQuery(SysTenant.class).in(SysTenant::getPackageId, packageIds);
            return tenantMapper.exists(lqw);
        });
    }

}
