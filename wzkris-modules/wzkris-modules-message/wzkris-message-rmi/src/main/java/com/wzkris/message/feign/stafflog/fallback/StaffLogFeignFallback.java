package com.wzkris.message.feign.stafflog.fallback;

import com.wzkris.message.feign.stafflog.StaffLogFeign;
import com.wzkris.message.feign.stafflog.req.LoginLogReq;
import com.wzkris.message.feign.stafflog.req.OperateLogReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;

import java.util.List;

@Slf4j
public class StaffLogFeignFallback implements FallbackFactory<StaffLogFeign> {

    @Override
    public StaffLogFeign create(Throwable cause) {
        return new StaffLogFeign() {
            @Override
            public void saveOperlogs(List<OperateLogReq> operateLogReqs) {
                log.error("saveOperlogs => req: {}", operateLogReqs, cause);
            }

            @Override
            public void saveLoginlog(LoginLogReq loginLogReq) {
                log.error("saveLoginlog => req: {}", loginLogReq, cause);
            }
        };
    }

}
