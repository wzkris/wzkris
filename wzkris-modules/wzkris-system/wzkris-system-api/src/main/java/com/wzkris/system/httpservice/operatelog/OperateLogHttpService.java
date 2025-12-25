package com.wzkris.system.httpservice.operatelog;

import com.wzkris.common.httpservice.annotation.HttpServiceClient;
import com.wzkris.common.httpservice.constants.ServiceIdConstant;
import com.wzkris.system.httpservice.operatelog.fallback.OperateLogHttpServiceFallback;
import com.wzkris.system.httpservice.operatelog.req.OperateLogEvent;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

import java.util.List;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : RPC -- 系统日志服务
 * @date : 2023/3/13 16:12
 */
@HttpServiceClient(
        serviceId = ServiceIdConstant.SYSTEM,
        fallbackFactory = OperateLogHttpServiceFallback.class
)
@HttpExchange(url = "/feign-operate-log")
public interface OperateLogHttpService {

    /**
     * 新增操作日志
     */
    @PostExchange("/save")
    void save(@RequestBody List<OperateLogEvent> operateLogEvents);

}
