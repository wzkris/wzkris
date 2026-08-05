package com.wzkris.payment.service.impl;

import com.wzkris.common.orm.plus.ServiceImplPlus;
import com.wzkris.payment.domain.PayChannelLogDO;
import com.wzkris.payment.mapper.PayChannelLogMapper;
import com.wzkris.payment.service.PayChannelLogService;
import org.springframework.stereotype.Service;

@Service
public class PayChannelLogServiceImpl
        extends ServiceImplPlus<PayChannelLogMapper, PayChannelLogDO>
        implements PayChannelLogService {
}
