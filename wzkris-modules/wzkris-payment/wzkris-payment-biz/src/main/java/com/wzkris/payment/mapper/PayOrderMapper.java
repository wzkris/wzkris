package com.wzkris.payment.mapper;

import com.wzkris.common.orm.plus.BaseMapperPlus;
import com.wzkris.payment.domain.PayOrderDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Mapper
@Repository
public interface PayOrderMapper extends BaseMapperPlus<PayOrderDO> {

    /**
     * 原子预留退款额度：仅订单 SUCCESS 且 refunded_amount + 本次 <= amount 时成功。
     * 并发护栏，返回影响行数 0 即超额或订单非成功。
     */
    @Update("UPDATE biz.pay_order SET refunded_amount = refunded_amount + #{amount} "
            + "WHERE pay_order_id = #{payOrderId} AND status = 'SUCCESS' "
            + "AND refunded_amount + #{amount} <= amount")
    int reserveRefund(Long payOrderId, BigDecimal amount);

    /**
     * 释放预留的退款额度（退款失败回退），带 refunded_amount >= amount 防下溢。
     */
    @Update("UPDATE biz.pay_order SET refunded_amount = refunded_amount - #{amount} "
            + "WHERE pay_order_id = #{payOrderId} AND refunded_amount >= #{amount}")
    int releaseRefund(Long payOrderId, BigDecimal amount);

}
