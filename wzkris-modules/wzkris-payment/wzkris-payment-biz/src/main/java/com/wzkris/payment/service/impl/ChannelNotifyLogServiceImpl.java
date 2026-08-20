package com.wzkris.payment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.orm.plus.ServiceImplPlus;
import com.wzkris.payment.domain.ChannelNotifyLogDO;
import com.wzkris.payment.enums.channel.PayChannelEnum;
import com.wzkris.payment.enums.notify.NotifyTypeEnum;
import com.wzkris.payment.mapper.ChannelNotifyLogMapper;
import com.wzkris.payment.service.ChannelNotifyLogService;
import org.springframework.stereotype.Service;

@Service
public class ChannelNotifyLogServiceImpl
        extends ServiceImplPlus<ChannelNotifyLogMapper, ChannelNotifyLogDO>
        implements ChannelNotifyLogService {

    @Override
    public ChannelNotifyLogDO findByChannelAndTypeAndOutBusinessNo(
            PayChannelEnum channel, NotifyTypeEnum notifyType, String outBusinessNo) {
        return this.getOne(new LambdaQueryWrapper<ChannelNotifyLogDO>()
                .eq(ChannelNotifyLogDO::getChannel, channel)
                .eq(ChannelNotifyLogDO::getNotifyType, notifyType)
                .eq(ChannelNotifyLogDO::getOutBusinessNo, outBusinessNo));
    }

}
