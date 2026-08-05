package com.wzkris.payment.service;

import com.wzkris.common.orm.plus.IServicePlus;
import com.wzkris.payment.domain.PayChannelNotifyDO;
import com.wzkris.payment.enums.channel.PayChannelEnum;
import com.wzkris.payment.enums.notify.NotifyTypeEnum;

/**
 * 渠道回调记录
 *
 * @author wzkris
 */
public interface PayChannelNotifyService extends IServicePlus<PayChannelNotifyDO> {

    PayChannelNotifyDO findByChannelAndTypeAndOutBusinessNo(
            PayChannelEnum channel, NotifyTypeEnum notifyType, String outBusinessNo);
}
