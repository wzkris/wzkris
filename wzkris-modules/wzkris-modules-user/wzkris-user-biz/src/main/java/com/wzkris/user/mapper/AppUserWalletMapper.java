package com.wzkris.user.mapper;

import com.wzkris.common.orm.plus.BaseMapperPlus;
import com.wzkris.user.domain.AppUserWallet;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

/**
 * 用户钱包表 数据层
 *
 * @author wzkris
 */
@Repository
public interface AppUserWalletMapper extends BaseMapperPlus<AppUserWallet> {

    // 增加余额
    @Update("UPDATE app_user_wallet SET balance = balance + #{amount} WHERE user_id = #{userId} AND #{amount} > 0")
    int incryBalance(@Param("userId") Long userId, @Param("amount") BigDecimal amount);

    // 扣减余额
    @Update("UPDATE app_user_wallet SET balance = balance - #{amount} WHERE user_id = #{userId} AND #{amount} > 0 AND balance >= #{amount}")
    int decryBalance(@Param("userId") Long userId, @Param("amount") BigDecimal amount);
}
