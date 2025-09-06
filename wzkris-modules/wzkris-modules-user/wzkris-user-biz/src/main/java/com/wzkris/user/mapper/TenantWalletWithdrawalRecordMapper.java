package com.wzkris.user.mapper;

import com.wzkris.common.orm.plus.BaseMapperPlus;
import com.wzkris.user.domain.TenantWalletWithdrawalRecordDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface TenantWalletWithdrawalRecordMapper extends BaseMapperPlus<TenantWalletWithdrawalRecordDO> {

}
