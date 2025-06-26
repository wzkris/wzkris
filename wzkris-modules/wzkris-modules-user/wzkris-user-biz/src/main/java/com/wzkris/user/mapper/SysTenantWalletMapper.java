package com.wzkris.user.mapper;

import com.wzkris.common.orm.plus.BaseMapperPlus;
import com.wzkris.user.domain.SysTenantWallet;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

/**
 * 租户钱包表 数据层
 *
 * @author wzkris
 */
@Repository
public interface SysTenantWalletMapper extends BaseMapperPlus<SysTenantWallet> {

    /**
     * 增加余额
     *
     * @param tenantId 租户ID
     * @param amount   元 > 0
     * @return
     */
    @Update(
            "UPDATE biz.sys_tenant_wallet SET balance = balance + #{amount} WHERE tenant_id = #{tenantId} AND #{amount} > 0")
    int incryBalance(Long tenantId, BigDecimal amount);

    /**
     * 扣减余额
     *
     * @param tenantId 租户ID
     * @param amount   元 > 0
     * @return
     */
    @Update(
            "UPDATE biz.sys_tenant_wallet SET balance = balance - #{amount} WHERE tenant_id = #{tenantId} AND #{amount} > 0 AND balance >= #{amount}")
    int decryBalance(Long tenantId, BigDecimal amount);

}
