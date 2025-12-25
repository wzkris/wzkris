package com.wzkris.system.httpservice.operatelog.fallback;

import com.wzkris.common.httpservice.fallback.HttpServiceFallback;
import com.wzkris.system.httpservice.operatelog.OperateLogHttpService;
import com.wzkris.system.httpservice.operatelog.req.OperateLogEvent;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class OperateLogHttpServiceFallback implements HttpServiceFallback<OperateLogHttpService> {

    @Override
    public OperateLogHttpService create(Throwable cause) {
        return new OperateLogHttpService() {
            @Override
            public void save(List<OperateLogEvent> operateLogEvents) {
                log.error("save => req: {}", operateLogEvents, cause);
            }
        };
    }

}
