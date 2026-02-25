package com.wzkris.system.httpclient.operatelog;

import com.wzkris.common.httpclient.annotation.HttpClient;
import com.wzkris.common.httpclient.constants.ServiceContextPathConstant;
import com.wzkris.common.httpclient.constants.ServiceIdConstant;
import com.wzkris.system.httpclient.operatelog.fallback.OperateLogClientFallback;
import com.wzkris.system.httpclient.operatelog.req.OperateLogEvent;
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
@HttpClient(
        serviceId = ServiceIdConstant.SYSTEM,
        path = ServiceContextPathConstant.SYSTEM,
        fallbackFactory = OperateLogClientFallback.class
)
@HttpExchange(url = "/operate-log-client")
public interface OperateLogClient {

    /**
     * 新增操作日志
     */
    @PostExchange("/save")
    void save(@RequestBody List<OperateLogEvent> operateLogEvents);

}
