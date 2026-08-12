package com.wzkris.payment.mapper;

import com.wzkris.common.orm.plus.BaseMapperPlus;
import com.wzkris.payment.domain.TenantBalanceWithdrawalLogDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface TenantBalanceWithdrawalLogMapper extends BaseMapperPlus<TenantBalanceWithdrawalLogDO> {

}