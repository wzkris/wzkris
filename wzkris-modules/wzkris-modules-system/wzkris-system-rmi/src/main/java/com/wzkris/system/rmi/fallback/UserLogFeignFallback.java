package com.wzkris.system.rmi.fallback;

import com.wzkris.system.rmi.UserLogFeign;
import com.wzkris.system.rmi.domain.req.LoginLogReq;
import com.wzkris.system.rmi.domain.req.OperateLogReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class UserLogFeignFallback implements FallbackFactory<UserLogFeign> {

    @Override
    public UserLogFeign create(Throwable cause) {
        return new UserLogFeign() {
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
