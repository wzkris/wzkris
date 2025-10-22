package com.wzkris.message.feign.userlog.fallback;

import com.wzkris.message.feign.userlog.UserLogFeign;
import com.wzkris.message.feign.userlog.req.UserLoginLogReq;
import com.wzkris.message.feign.userlog.req.UserOperateLogReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;

import java.util.List;

@Slf4j
public class UserLogFeignFallback implements FallbackFactory<UserLogFeign> {

    @Override
    public UserLogFeign create(Throwable cause) {
        return new UserLogFeign() {
            @Override
            public void saveOperlogs(List<UserOperateLogReq> userOperateLogReqs) {
                log.error("saveOperlogs => req: {}", userOperateLogReqs, cause);
            }

            @Override
            public void saveLoginlog(UserLoginLogReq userLoginLogReq) {
                log.error("saveLoginlog => req: {}", userLoginLogReq, cause);
            }
        };
    }

}
