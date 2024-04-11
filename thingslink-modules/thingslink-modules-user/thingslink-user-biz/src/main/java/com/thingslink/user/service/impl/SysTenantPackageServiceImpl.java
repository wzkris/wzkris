package com.thingslink.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.thingslink.user.domain.SysTenantPackage;
import com.thingslink.user.mapper.SysTenantPackageMapper;
import com.thingslink.user.service.SysTenantPackageService;
import com.thingslink.common.core.utils.StringUtil;
import com.thingslink.common.orm.page.Page;
import com.thingslink.common.orm.utils.PageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SysTenantPackageServiceImpl implements SysTenantPackageService {

    private final SysTenantPackageMapper tenantPackageMapper;

    /**
     * 分页查询
     *
     * @param sysTenantPackage 查询条件
     */
    @Override
    public Page<SysTenantPackage> listPage(SysTenantPackage sysTenantPackage) {
        PageUtil.startPage();
        // 查询
        LambdaQueryWrapper<SysTenantPackage> lqw = this.buildQueryWrapper(sysTenantPackage);
        List<SysTenantPackage> packages = tenantPackageMapper.selectList(lqw);

        // 先获取分页信息，否则总数会有问题
        Page page = PageUtil.getPage();
        page.setRows(packages);
        return page;
    }


    private LambdaQueryWrapper<SysTenantPackage> buildQueryWrapper(SysTenantPackage sysTenantPackage) {
        return new LambdaQueryWrapper<SysTenantPackage>()
                .like(StringUtil.isNotNull(sysTenantPackage.getPackageName()), SysTenantPackage::getPackageName, sysTenantPackage.getPackageName())
                .eq(StringUtil.isNotNull(sysTenantPackage.getStatus()), SysTenantPackage::getStatus, sysTenantPackage.getStatus())
                .orderByDesc(SysTenantPackage::getPackageId);
    }
}
