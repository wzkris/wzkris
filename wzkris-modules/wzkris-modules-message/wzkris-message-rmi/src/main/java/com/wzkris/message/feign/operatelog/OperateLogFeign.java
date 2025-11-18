package com.wzkris.message.feign.operatelog;

import com.wzkris.common.openfeign.constants.ServiceIdConstant;
import com.wzkris.message.feign.operatelog.fallback.OperateLogFeignFallback;
import com.wzkris.message.feign.operatelog.req.OperateLogEvent;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : RPC -- 系统日志服务
 * @date : 2023/3/13 16:12
 */
@FeignClient(name = ServiceIdConstant.MESSAGE, contextId = "OperateLogFeign",
        fallbackFactory = OperateLogFeignFallback.class,
        path = "/feign-operate-log")
public interface OperateLogFeign {

    /**
     * 新增操作日志
     */
    @PostMapping("/save")
    void save(@RequestBody List<OperateLogEvent> operateLogEvents);

}
