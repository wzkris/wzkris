package com.thingslink.order.service;

import com.thingslink.order.domain.ChargingOrder;
import com.thingslink.order.enums.ChargingEvent;

/**
 * 订单信息(Order)表服务接口
 *
 * @author wzkris
 * @since 2023-04-17 16:32:38
 */
public interface ChargingOrderService {

    boolean changeStatus(ChargingEvent event, ChargingOrder order);
}
