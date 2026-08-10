package com.wzkris.payment.service;

import com.wzkris.common.orm.plus.IServicePlus;
import com.wzkris.payment.domain.ChannelNotifyLogDO;
import com.wzkris.payment.enums.channel.PayChannelEnum;
import com.wzkris.payment.enums.notify.NotifyTypeEnum;

/**
 * 渠道回调记录
 *
 * @author wzkris
 */
public interface ChannelNotifyLogService extends IServicePlus<ChannelNotifyLogDO> {

    ChannelNotifyLogDO findByChannelAndTypeAndOutBusinessNo(
            PayChannelEnum channel, NotifyTypeEnum notifyType, String outBusinessNo);

}
