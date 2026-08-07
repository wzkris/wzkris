package com.wzkris.payment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.orm.plus.ServiceImplPlus;
import com.wzkris.payment.domain.PayChannelNotifyDO;
import com.wzkris.payment.enums.channel.PayChannelEnum;
import com.wzkris.payment.enums.notify.NotifyTypeEnum;
import com.wzkris.payment.mapper.PayChannelNotifyMapper;
import com.wzkris.payment.service.PayChannelNotifyService;
import org.springframework.stereotype.Service;

@Service
public class PayChannelNotifyServiceImpl
        extends ServiceImplPlus<PayChannelNotifyMapper, PayChannelNotifyDO>
        implements PayChannelNotifyService {

    @Override
    public PayChannelNotifyDO findByChannelAndTypeAndOutBusinessNo(
            PayChannelEnum channel, NotifyTypeEnum notifyType, String outBusinessNo) {
        return this.getOne(new LambdaQueryWrapper<PayChannelNotifyDO>()
                .eq(PayChannelNotifyDO::getChannel, channel)
                .eq(PayChannelNotifyDO::getNotifyType, notifyType)
                .eq(PayChannelNotifyDO::getOutBusinessNo, outBusinessNo));
    }

}
