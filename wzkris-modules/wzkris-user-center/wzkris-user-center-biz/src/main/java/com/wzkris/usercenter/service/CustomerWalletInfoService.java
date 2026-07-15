package com.wzkris.usercenter.service;

import com.wzkris.common.orm.plus.IServicePlus;
import com.wzkris.usercenter.domain.CustomerWalletInfoDO;

import java.math.BigDecimal;

public interface CustomerWalletInfoService extends IServicePlus<CustomerWalletInfoDO> {

    /**
     * 增加余额
     *
     * @param customerId 用户ID
     * @param amount     金额
     * @return 是否成功
     */
    boolean incryBalance(Long customerId, BigDecimal amount);

    /**
     * 扣减余额
     *
     * @param customerId 用户ID
     * @param amount     金额
     * @return 是否成功
     */
    boolean decryBalance(Long customerId, BigDecimal amount);

}
