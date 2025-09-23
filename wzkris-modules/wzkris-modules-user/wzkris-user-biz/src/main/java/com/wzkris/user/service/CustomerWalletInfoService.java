package com.wzkris.user.service;

import java.math.BigDecimal;

public interface CustomerWalletInfoService {

    /**
     * 增加余额
     *
     * @param customerId 用户ID
     * @param amount 金额
     * @return 是否成功
     */
    boolean incryBalance(Long customerId, BigDecimal amount);

    /**
     * 扣减余额
     *
     * @param customerId 用户ID
     * @param amount 金额
     * @return 是否成功
     */
    boolean decryBalance(Long customerId, BigDecimal amount);

}
