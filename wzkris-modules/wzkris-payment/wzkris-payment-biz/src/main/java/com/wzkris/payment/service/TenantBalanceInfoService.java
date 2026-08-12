package com.wzkris.payment.service;

import com.wzkris.common.orm.plus.IServicePlus;
import com.wzkris.payment.domain.TenantBalanceInfoDO;

import java.math.BigDecimal;

public interface TenantBalanceInfoService extends IServicePlus<TenantBalanceInfoDO> {

    /**
     * 获取账户，不存在时懒创建（按 tenantId 归属者字段查询）
     */
    TenantBalanceInfoDO getOrCreate(Long tenantId);

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