package com.wzkris.user.service;


import com.wzkris.user.domain.SysTenantPackage;

import java.util.List;

/**
 * 租户套餐层
 *
 * @author wzkris
 */
public interface SysTenantPackageService {
    /**
     * 分页查询
     *
     * @param sysTenantPackage 查询条件
     */
    List<SysTenantPackage> list(SysTenantPackage sysTenantPackage);

    /**
     * 校验套餐是否被使用
     *
     * @param packageIds 套餐ID
     * @return 结果
     */
    boolean checkPackageUsed(List<Long> packageIds);
}
