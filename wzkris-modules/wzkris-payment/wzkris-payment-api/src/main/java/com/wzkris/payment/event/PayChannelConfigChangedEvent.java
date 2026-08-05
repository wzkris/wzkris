package com.wzkris.payment.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 渠道配置变更事件（进程内解耦：配置增删改后通知渠道SDK缓存失效，如微信WxPayService按configId清除）
 *
 * @author wzkris
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PayChannelConfigChangedEvent {

    private Long configId;
}
