package com.wzkris.payment.mapper;

import com.wzkris.common.orm.plus.BaseMapperPlus;
import com.wzkris.payment.domain.PayChannelLogDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface PayChannelLogMapper extends BaseMapperPlus<PayChannelLogDO> {

}
