package com.wzkris.user.service;

import java.math.BigDecimal;

public interface AppUserWalletService {

    /**
     * 增加余额
     *
     * @param userId 用户ID
     * @param amount 金额
     * @return 是否成功
     */
    boolean incryBalance(Long userId, BigDecimal amount);

    /**
     * 扣减余额
     *
     * @param userId 用户ID
     * @param amount 金额
     * @return 是否成功
     */
    boolean decryBalance(Long userId, BigDecimal amount);
}
