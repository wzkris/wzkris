package com.wzkris.payment.mapper;

import com.wzkris.common.orm.plus.BaseMapperPlus;
import com.wzkris.payment.domain.TenantBalanceTransactionLogDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 租户资金流水表 数据层
 *
 * @author wzkris
 */
@Mapper
@Repository
public interface TenantBalanceTransactionLogMapper extends BaseMapperPlus<TenantBalanceTransactionLogDO> {

}