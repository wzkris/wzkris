package com.wzkris.user.service;


import com.wzkris.user.domain.dto.SysTenantDTO;

import java.util.List;

/**
 * 租户层
 *
 * @author wzkris
 */
public interface SysTenantService {

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
    void deleteByIds(List<Long> tenantIds);

    /**
     * 校验租户账号数量
     *
     * @param tenantId 租户ID
     * @return true通过 false不通过
     */
    boolean checkAccountLimit(Long tenantId);

    /**
     * 校验租户角色数量
     *
     * @param tenantId 租户ID
     * @return true通过 false不通过
     */
    boolean checkRoleLimit(Long tenantId);

    /**
     * 校验租户岗位数量
     *
     * @param tenantId 租户ID
     * @return true通过 false不通过
     */
    boolean checkPostLimit(Long tenantId);

    /**
     * 校验租户部门数量
     *
     * @param tenantId 租户ID
     * @return true通过 false不通过
     */
    boolean checkDeptLimit(Long tenantId);

    /**
     * 校验是否租户超管
     *
     * @param userIds 用户ID
     * @return true通过 false不通过
     */
    boolean checkAdministrator(List<Long> userIds);
}
