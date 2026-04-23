package com.wzkris.common.log.remote;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.log.remote.request.OperateLogEvent;
import com.wzkris.common.remote.annotation.RemoteInterface;
import com.wzkris.common.remote.constants.ServiceContextPathConstant;
import com.wzkris.common.remote.constants.ServiceIdConstant;
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
@RemoteInterface(
        serviceId = ServiceIdConstant.SYSTEM,
        path = ServiceContextPathConstant.SYSTEM
)
@HttpExchange(url = "/operate-log-remote")
public interface IOperateLogRemote {

    /**
     * 新增操作日志
     */
    @PostExchange("/save")
    Result<Void> save(@RequestBody List<OperateLogEvent> operateLogEvents);

}
