package com.wzkris.payment.service;

import com.wzkris.common.orm.plus.IServicePlus;
import com.wzkris.payment.domain.CustomerBalanceInfoDO;

import java.math.BigDecimal;

public interface CustomerBalanceInfoService extends IServicePlus<CustomerBalanceInfoDO> {

    /**
     * 获取账户，不存在时懒创建（按 customerId 归属者字段查询）
     */
    CustomerBalanceInfoDO getOrCreate(Long customerId);

    /**
     * 增加余额
     *
     * @param customerId 用户ID
     * @param amount     金额元
     * @param bizNo      业务编号
     * @param bizType    业务类型
     * @param remark     流水备注
     * @return 是否成功
     */
    boolean incryBalance(Long customerId, BigDecimal amount, String bizNo, String bizType, String remark);

    /**
     * 扣减余额
     *
     * @param customerId 用户ID
     * @param amount     金额元
     * @param bizNo      业务编号
     * @param bizType    业务类型
     * @param remark     流水备注
     * @return 是否成功
     */
    boolean decryBalance(Long customerId, BigDecimal amount, String bizNo, String bizType, String remark);

}