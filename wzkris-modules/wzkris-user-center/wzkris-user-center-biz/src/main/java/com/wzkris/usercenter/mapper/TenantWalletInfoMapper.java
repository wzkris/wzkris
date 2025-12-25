package com.wzkris.usercenter.mapper;

import com.wzkris.common.orm.plus.BaseMapperPlus;
import com.wzkris.usercenter.domain.TenantWalletInfoDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

/**
 * 租户钱包表 数据层
 *
 * @author wzkris
 */
@Mapper
@Repository
public interface TenantWalletInfoMapper extends BaseMapperPlus<TenantWalletInfoDO> {

    /**
     * 增加余额
     *
     * @param tenantId 租户ID
     * @param amount   元 > 0
     * @return
     */
    @Update("UPDATE biz.tenant_wallet_info SET balance = balance + #{amount} WHERE tenant_id = #{tenantId} AND #{amount} > 0")
    int incryBalance(Long tenantId, BigDecimal amount);

    /**
     * 扣减余额
     *
     * @param tenantId 租户ID
     * @param amount   元 > 0
     * @return
     */
    @Update("UPDATE biz.tenant_wallet_info SET balance = balance - #{amount} WHERE tenant_id = #{tenantId} AND #{amount} > 0 AND balance >= #{amount}")
    int decryBalance(Long tenantId, BigDecimal amount);

}
