package com.wzkris.payment.mapper;

import com.wzkris.common.orm.plus.BaseMapperPlus;
import com.wzkris.payment.domain.CustomerBalanceTransactionLogDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 用户资金流水表 数据层
 *
 * @author wzkris
 */
@Mapper
@Repository
public interface CustomerBalanceTransactionLogMapper extends BaseMapperPlus<CustomerBalanceTransactionLogDO> {

}