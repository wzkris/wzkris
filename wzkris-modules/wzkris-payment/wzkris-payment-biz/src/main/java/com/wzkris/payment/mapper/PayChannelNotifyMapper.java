package com.wzkris.payment.mapper;

import com.wzkris.common.orm.plus.BaseMapperPlus;
import com.wzkris.payment.domain.PayChannelNotifyDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface PayChannelNotifyMapper extends BaseMapperPlus<PayChannelNotifyDO> {

}
