package com.wzkris.principal.mapper;

import com.wzkris.common.orm.plus.BaseMapperPlus;
import com.wzkris.principal.domain.TenantWalletWithdrawalRecordDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface TenantWalletWithdrawalRecordMapper extends BaseMapperPlus<TenantWalletWithdrawalRecordDO> {

}
