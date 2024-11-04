package com.wzkris.user.service;


import com.wzkris.user.domain.SysTenant;
import com.wzkris.user.domain.dto.SysTenantDTO;

import java.util.List;

/**
 * 租户层
 *
 * @author wzkris
 */
public interface SysTenantService {

    /**
     * 查询
     *
     * @param sysTenant 查询条件
     */
    List<SysTenant> list(SysTenant sysTenant);

    /**
     * 添加租户, 会创建租户管理员账号
     *
     * @param tenantDTO 参数
     */
    void insertTenant(SysTenantDTO tenantDTO);

    /**
     * 更新租户, 不会更新租户管理员账号
     *
     * @param sysTenant 参数
     */
    boolean updateTenant(SysTenant sysTenant);
}
