package com.wzkris.common.notifier.core;

import com.wzkris.common.notifier.enums.NotificationChannelEnum;

/**
 * 通知器接口
 */
public interface Notifier<T> {

    NotificationResult send(T message);

    NotificationChannelEnum getChannel();

    T buildMessage(NotificationContext context);

}
