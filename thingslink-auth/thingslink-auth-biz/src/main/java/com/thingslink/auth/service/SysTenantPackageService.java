package com.thingslink.auth.service;


import com.thingslink.auth.domain.SysTenantPackage;
import com.thingslink.common.orm.page.Page;

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
