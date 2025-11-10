package com.wzkris.message.feign.tenantlog.fallback;

import com.wzkris.message.feign.tenantlog.TenantLogFeign;
import com.wzkris.message.feign.tenantlog.req.LoginLogReq;
import com.wzkris.message.feign.tenantlog.req.OperateLogReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;

import java.util.List;

@Slf4j
public class TenantLogFeignFallback implements FallbackFactory<TenantLogFeign> {

    @Override
    public TenantLogFeign create(Throwable cause) {
        return new TenantLogFeign() {
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
