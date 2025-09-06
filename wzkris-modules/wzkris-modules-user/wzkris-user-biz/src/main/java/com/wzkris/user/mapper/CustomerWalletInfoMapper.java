package com.wzkris.user.mapper;

import com.wzkris.common.orm.plus.BaseMapperPlus;
import com.wzkris.user.domain.CustomerWalletInfoDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

/**
 * 用户钱包表 数据层
 *
 * @author wzkris
 */
@Mapper
@Repository
public interface CustomerWalletInfoMapper extends BaseMapperPlus<CustomerWalletInfoDO> {

    /**
     * 增加余额
     *
     * @param userId 用户ID
     * @param amount 元 > 0
     * @return
     */
    @Update("UPDATE biz.customer_wallet_info SET balance = balance + #{amount} WHERE user_id = #{userId} AND #{amount} > 0")
    int incryBalance(Long userId, BigDecimal amount);

    /**
     * 扣减余额
     *
     * @param userId 用户ID
     * @param amount 元 > 0
     * @return
     */
    @Update("UPDATE biz.customer_wallet_info SET balance = balance - #{amount} WHERE user_id = #{userId} AND #{amount} > 0 AND balance >= #{amount}")
    int decryBalance(Long userId, BigDecimal amount);

}
