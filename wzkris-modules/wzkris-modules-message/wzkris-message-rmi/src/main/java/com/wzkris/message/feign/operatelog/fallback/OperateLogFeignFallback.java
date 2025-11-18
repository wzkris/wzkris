package com.wzkris.message.feign.operatelog.fallback;

import com.wzkris.message.feign.operatelog.OperateLogFeign;
import com.wzkris.message.feign.operatelog.req.OperateLogEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;

import java.util.List;

@Slf4j
public class OperateLogFeignFallback implements FallbackFactory<OperateLogFeign> {

    @Override
    public OperateLogFeign create(Throwable cause) {
        return new OperateLogFeign() {
            @Override
            public void save(List<OperateLogEvent> operateLogEvents) {
                log.error("save => req: {}", operateLogEvents, cause);
            }
        };
    }

}
