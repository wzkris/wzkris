package com.wzkris.user.service;

import java.math.BigDecimal;

public interface SysTenantWalletService {

    /**
     * 增加余额
     *
     * @param tenantId 租户ID
     * @param amount   金额元
     * @param bizNo    业务编号
     * @param bizType  业务类型
     * @param remark   流水备注
     * @return 是否成功
     */
    boolean incryBalance(Long tenantId, BigDecimal amount, String bizNo, String bizType, String remark);

    /**
     * 扣减余额
     *
     * @param tenantId 租户ID
     * @param amount   金额元
     * @param bizNo    业务编号
     * @param bizType  业务类型
     * @param remark   流水备注
     * @return 是否成功
     */
    boolean decryBalance(Long tenantId, BigDecimal amount, String bizNo, String bizType, String remark);

}
