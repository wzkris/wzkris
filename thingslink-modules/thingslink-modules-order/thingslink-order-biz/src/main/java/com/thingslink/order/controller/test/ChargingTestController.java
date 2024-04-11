package com.thingslink.order.controller.test;

import com.thingslink.common.core.domain.Result;
import com.thingslink.order.domain.ChargingOrder;
import com.thingslink.order.enums.ChargingEvent;
import com.thingslink.order.mapper.ChargingOrderMapper;
import com.thingslink.order.service.ChargingOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 测试
 * @date : 2023/12/4 11:11
 */
@RestController
@RequestMapping("/charging_test")
public class ChargingTestController {

    @Autowired
    private ChargingOrderService chargingOrderService;

    @Autowired
    private ChargingOrderMapper chargingOrderMapper;

    @RequestMapping("/test")
    public Result test(Long orderId) {

        ChargingOrder chargingOrder = chargingOrderMapper.getById(orderId);

        boolean suc = chargingOrderService.changeStatus(ChargingEvent.PAY, chargingOrder);

        return Result.toRes(suc);
    }
}
