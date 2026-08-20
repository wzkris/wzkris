package com.wzkris.payment.mapper;

import com.wzkris.common.orm.plus.BaseMapperPlus;
import com.wzkris.payment.domain.CustomerBalanceInfoDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

/**
 * 用户账户表 数据层
 *
 * @author wzkris
 */
@Mapper
@Repository
public interface CustomerBalanceInfoMapper extends BaseMapperPlus<CustomerBalanceInfoDO> {

    /**
     * 增加余额（原子入账，累加累计入账）
     *
     * @param customerId 用户ID
     * @param amount     元 > 0
     * @return 变动后余额；守卫失败返回 null
     */
    @Select("""
            UPDATE biz.customer_balance_info
            SET balance = balance + #{amount}, total_in = total_in + #{amount}
            WHERE customer_id = #{customerId} AND #{amount} > 0
            RETURNING balance
            """)
    BigDecimal incryBalance(Long customerId, BigDecimal amount);

    /**
     * 扣减余额（原子守卫：可用余额充足才扣，累加累计支出）
     *
     * @param customerId 用户ID
     * @param amount     元 > 0
     * @return 变动后余额；守卫失败返回 null
     */
    @Select("""
            UPDATE biz.customer_balance_info
            SET balance = balance - #{amount}, total_out = total_out + #{amount}
            WHERE customer_id = #{customerId} AND #{amount} > 0 AND balance >= #{amount}
            RETURNING balance
            """)
    BigDecimal decryBalance(Long customerId, BigDecimal amount);

}