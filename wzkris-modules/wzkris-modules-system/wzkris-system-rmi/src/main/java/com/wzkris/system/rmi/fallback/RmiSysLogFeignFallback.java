package com.wzkris.system.rmi.fallback;

import com.wzkris.common.openfeign.core.FeignLogAggregator;
import com.wzkris.system.rmi.RmiSysLogFeign;
import com.wzkris.system.rmi.domain.req.LoginLogReq;
import com.wzkris.system.rmi.domain.req.OperLogReq;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RmiSysLogFeignFallback implements FallbackFactory<RmiSysLogFeign> {

    @Override
    public RmiSysLogFeign create(Throwable cause) {
        return new RmiSysLogFeign() {
            @Override
            public void saveOperlogs(List<OperLogReq> operLogReqs) {
                FeignLogAggregator.INSTANCE.logPrintError(this.getClass(), cause);
            }

            @Override
            public void saveLoginlog(LoginLogReq loginLogReq) {
                FeignLogAggregator.INSTANCE.logPrintError(this.getClass(), cause);
            }
        };
    }

}
