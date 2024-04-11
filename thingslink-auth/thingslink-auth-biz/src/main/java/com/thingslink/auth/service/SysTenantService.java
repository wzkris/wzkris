package com.thingslink.auth.service;


import com.thingslink.auth.domain.SysTenant;
import com.thingslink.auth.domain.dto.SysTenantDTO;
import com.thingslink.common.orm.page.Page;

/**
 * 租户套餐层
 *
 * @author wzkris
 */
public interface SysTenantService {

    /**
     * 分页查询
     *
     * @param sysTenant 查询条件
     */
    Page<SysTenant> listPage(SysTenant sysTenant);

    /**
     * 添加租户
     *
     * @param tenantDTO 参数
     */
    boolean insertTenant(SysTenantDTO tenantDTO);

    /**
     * 更新租户
     *
     * @param sysTenant 参数
     */
    boolean updateTenant(SysTenant sysTenant);
}
