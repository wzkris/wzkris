package com.wzkris.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wzkris.common.core.constant.CommonConstants;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.orm.utils.DynamicTenantUtil;
import com.wzkris.user.domain.TenantInfoDO;
import com.wzkris.user.domain.TenantPackageInfoDO;
import com.wzkris.user.domain.vo.SelectVO;
import com.wzkris.user.mapper.TenantInfoMapper;
import com.wzkris.user.mapper.TenantPackageInfoMapper;
import com.wzkris.user.service.TenantPackageInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TenantPackageInfoServiceImpl implements TenantPackageInfoService {

    private final TenantInfoMapper tenantInfoMapper;

    private final TenantPackageInfoMapper tenantPackageInfoMapper;

    @Override
    public List<SelectVO> listSelect(String packageName) {
        LambdaQueryWrapper<TenantPackageInfoDO> lqw = new LambdaQueryWrapper<TenantPackageInfoDO>()
                .select(TenantPackageInfoDO::getPackageId, TenantPackageInfoDO::getPackageName)
                .eq(TenantPackageInfoDO::getStatus, CommonConstants.STATUS_ENABLE)
                .like(StringUtil.isNotBlank(packageName), TenantPackageInfoDO::getPackageName, packageName)
                .orderByAsc(TenantPackageInfoDO::getPackageId);
        return tenantPackageInfoMapper.selectList(lqw).stream().map(SelectVO::new).toList();
    }

    @Override
    public boolean checkPackageUsed(List<Long> packageIds) {
        return DynamicTenantUtil.ignore(() -> {
            LambdaQueryWrapper<TenantInfoDO> lqw =
                    Wrappers.lambdaQuery(TenantInfoDO.class).in(TenantInfoDO::getPackageId, packageIds);
            return tenantInfoMapper.exists(lqw);
        });
    }

}
