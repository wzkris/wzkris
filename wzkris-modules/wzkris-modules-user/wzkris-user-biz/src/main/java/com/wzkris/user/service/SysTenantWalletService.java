package com.wzkris.user.service;

import java.math.BigDecimal;

public interface SysTenantWalletService {

    /**
     * 增加余额
     *
     * @param tenantId 租户ID
     * @param amount   金额
     * @return 是否成功
     */
    boolean incryBalance(Long tenantId, BigDecimal amount);

    /**
     * 扣减余额
     *
     * @param tenantId 租户ID
     * @param amount   金额
     * @return 是否成功
     */
    boolean decryBalance(Long tenantId, BigDecimal amount);

}
