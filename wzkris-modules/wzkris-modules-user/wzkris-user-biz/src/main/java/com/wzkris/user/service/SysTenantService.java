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
     * 删除租户及相关信息(hard delete)
     *
     * @param tenantIds 租户ID集合
     */
    void hardDeleteByIds(List<Long> tenantIds);
}
