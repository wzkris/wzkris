package com.wzkris.message.feign.stafflog.fallback;

import com.wzkris.message.feign.stafflog.StaffLogFeign;
import com.wzkris.message.feign.stafflog.req.StaffLoginLogReq;
import com.wzkris.message.feign.stafflog.req.StaffOperateLogReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;

import java.util.List;

@Slf4j
public class StaffLogFeignFallback implements FallbackFactory<StaffLogFeign> {

    @Override
    public StaffLogFeign create(Throwable cause) {
        return new StaffLogFeign() {
            @Override
            public void saveOperlogs(List<StaffOperateLogReq> operateLogReqs) {
                log.error("saveOperlogs => req: {}", operateLogReqs, cause);
            }

            @Override
            public void saveLoginlog(StaffLoginLogReq loginLogReq) {
                log.error("saveLoginlog => req: {}", loginLogReq, cause);
            }
        };
    }

}
