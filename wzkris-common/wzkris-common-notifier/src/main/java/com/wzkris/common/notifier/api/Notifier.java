package com.wzkris.common.notifier.api;

import com.wzkris.common.notifier.domain.NotificationResult;
import com.wzkris.common.notifier.enums.NotificationChannelEnum;

/**
 * 通知器接口
 */
public interface Notifier<T> {

    NotificationResult send(T message);

    NotificationChannelEnum getChannel();

}
