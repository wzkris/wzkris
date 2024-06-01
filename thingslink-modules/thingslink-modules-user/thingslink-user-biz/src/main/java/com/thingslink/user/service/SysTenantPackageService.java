package com.thingslink.user.service;


import com.thingslink.common.orm.page.Page;
import com.thingslink.user.domain.SysTenantPackage;

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
    Page<SysTenantPackage> listPage(SysTenantPackage sysTenantPackage);

}
