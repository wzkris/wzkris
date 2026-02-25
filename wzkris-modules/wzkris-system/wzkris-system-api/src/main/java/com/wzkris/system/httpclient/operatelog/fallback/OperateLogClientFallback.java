package com.wzkris.system.httpclient.operatelog.fallback;

import com.wzkris.common.httpclient.fallback.HttpClientFallback;
import com.wzkris.system.httpclient.operatelog.OperateLogClient;
import com.wzkris.system.httpclient.operatelog.req.OperateLogEvent;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class OperateLogClientFallback implements HttpClientFallback<OperateLogClient> {

    @Override
    public OperateLogClient create(Throwable cause) {
        return new OperateLogClient() {
            @Override
            public void save(List<OperateLogEvent> operateLogEvents) {
                log.error("save => req: {}", operateLogEvents, cause);
            }
        };
    }

}
