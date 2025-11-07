package com.wzkris.message.feign.adminlog.fallback;

import com.wzkris.message.feign.adminlog.AdminLogFeign;
import com.wzkris.message.feign.adminlog.req.LoginLogReq;
import com.wzkris.message.feign.adminlog.req.OperateLogReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;

import java.util.List;

@Slf4j
public class AdminLogFeignFallback implements FallbackFactory<AdminLogFeign> {

    @Override
    public AdminLogFeign create(Throwable cause) {
        return new AdminLogFeign() {
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
