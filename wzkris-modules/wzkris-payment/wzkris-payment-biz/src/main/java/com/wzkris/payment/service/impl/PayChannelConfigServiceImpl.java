package com.wzkris.payment.service.impl;

import com.wzkris.common.orm.plus.ServiceImplPlus;
import com.wzkris.payment.domain.PayChannelConfigDO;
import com.wzkris.payment.mapper.PayChannelConfigMapper;
import com.wzkris.payment.service.PayChannelConfigService;
import org.springframework.stereotype.Service;

@Service
public class PayChannelConfigServiceImpl
        extends ServiceImplPlus<PayChannelConfigMapper, PayChannelConfigDO>
        implements PayChannelConfigService {
}
