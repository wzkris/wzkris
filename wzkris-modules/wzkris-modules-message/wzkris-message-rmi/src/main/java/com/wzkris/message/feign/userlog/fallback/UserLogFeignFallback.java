package com.wzkris.message.feign.userlog.fallback;

import com.wzkris.message.feign.userlog.UserLogFeign;
import com.wzkris.message.feign.userlog.req.LoginLogReq;
import com.wzkris.message.feign.userlog.req.OperateLogReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;

import java.util.List;

@Slf4j
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
