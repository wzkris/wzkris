package com.wzkris.usercenter.service;

import com.wzkris.common.orm.plus.IServicePlus;
import com.wzkris.usercenter.domain.TenantInfoDO;

import java.util.Collections;
import java.util.List;

/**
 * 租户层
 *
 * @author wzkris
 */
public interface TenantInfoService extends IServicePlus<TenantInfoDO> {

    /**
     * 添加租户, 会创建租户管理员账号
     *
     * @param tenant   参数
     * @param username 登录账户
     * @param password 登录密码
     */
    boolean saveTenant(TenantInfoDO tenant, String username, String password);

    /**
     * 删除租户及相关信息
     *
     * @param tenantId 租户ID
     */
    boolean removeTenant(Long tenantId);

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
     * 校验是否租户超管
     *
     * @param tenantUserIds 用户ID
     */
    boolean checkAdministrator(List<Long> tenantUserIds);

    default boolean checkAdministrator(Long tenantUserId) {
        return this.checkAdministrator(Collections.singletonList(tenantUserId));
    }

}

