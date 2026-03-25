package com.wzkris.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wzkris.common.core.constant.CommonConstants;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.orm.utils.SkipTenantInterceptorUtil;
import com.wzkris.usercenter.domain.TenantInfoDO;
import com.wzkris.usercenter.domain.TenantPackageInfoDO;
import com.wzkris.usercenter.mapper.TenantInfoMapper;
import com.wzkris.usercenter.mapper.TenantPackageInfoMapper;
import com.wzkris.usercenter.response.SelectResponse;
import com.wzkris.usercenter.service.TenantPackageInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TenantPackageInfoServiceImpl
        extends ServiceImpl<TenantPackageInfoMapper, TenantPackageInfoDO>
        implements TenantPackageInfoService {

    private final TenantInfoMapper tenantInfoMapper;

    @Override
    public List<SelectResponse> listSelect(String packageName) {
        LambdaQueryWrapper<TenantPackageInfoDO> lqw = new LambdaQueryWrapper<TenantPackageInfoDO>()
                .select(TenantPackageInfoDO::getPackageId, TenantPackageInfoDO::getPackageName)
                .eq(TenantPackageInfoDO::getStatus, CommonConstants.STATUS_ENABLE)
                .like(StringUtil.isNotBlank(packageName), TenantPackageInfoDO::getPackageName, packageName)
                .orderByAsc(TenantPackageInfoDO::getPackageId);
        return baseMapper.selectList(lqw).stream().map(packageInfoDO -> {
            SelectResponse SelectResponse = new SelectResponse();
            SelectResponse.setId(packageInfoDO.getPackageId());
            SelectResponse.setLabel(packageInfoDO.getPackageName());
            return SelectResponse;
        }).toList();
    }

    @Override
    public boolean existInUsed(List<Long> packageIds) {
        return SkipTenantInterceptorUtil.ignore(() -> {
            LambdaQueryWrapper<TenantInfoDO> lqw =
                    Wrappers.lambdaQuery(TenantInfoDO.class).in(TenantInfoDO::getPackageId, packageIds);
            return tenantInfoMapper.exists(lqw);
        });
    }

}

