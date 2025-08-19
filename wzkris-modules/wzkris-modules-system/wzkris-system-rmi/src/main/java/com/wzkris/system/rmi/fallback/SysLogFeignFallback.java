package com.wzkris.system.rmi.fallback;

import com.wzkris.system.rmi.SysLogFeign;
import com.wzkris.system.rmi.domain.req.LoginLogReq;
import com.wzkris.system.rmi.domain.req.OperLogReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class SysLogFeignFallback implements FallbackFactory<SysLogFeign> {

    @Override
    public SysLogFeign create(Throwable cause) {
        return new SysLogFeign() {
            @Override
            public void saveOperlogs(List<OperLogReq> operLogReqs) {
                log.error("saveOperlogs => req: {}", operLogReqs, cause);
            }

            @Override
            public void saveLoginlog(LoginLogReq loginLogReq) {
                log.error("saveLoginlog => req: {}", loginLogReq, cause);
            }
        };
    }

}
