package com.thingslink.order.controller.test;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.thingslink.common.core.domain.Result;
import com.thingslink.common.core.enums.BizCode;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.thingslink.common.core.domain.Result.resp;
import static com.thingslink.common.core.domain.Result.success;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 限流测试
 * @date : 2023/12/7 14:54
 */
@RestController
@RequestMapping("/flow_test")
public class FlowLimitController {

    static int i = 0;

    @PostMapping("/test")
    @SentinelResource(value = "test", blockHandler = "handleException", fallback = "fallback")
    public Result test() {
        i++;
        if (i % 2 == 0) {
            throw new RuntimeException("限流测试");
        }
        return success();
    }

    // 流量控制异常处理
    public Result handleException(BlockException blockException) {
        return resp(BizCode.LIMIT_FLOW, "流量控制异常处理");
    }

    // 业务异常降级处理
    public Result fallback(Throwable throwable) {
        return resp(BizCode.INTERNAL_ERROR, "业务异常降级处理");
    }

}
