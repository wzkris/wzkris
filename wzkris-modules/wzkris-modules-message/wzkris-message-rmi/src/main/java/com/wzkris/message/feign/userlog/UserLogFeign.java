package com.wzkris.message.feign.userlog;

import com.wzkris.common.openfeign.constants.ServiceIdConstant;
import com.wzkris.message.feign.userlog.fallback.UserLogFeignFallback;
import com.wzkris.message.feign.userlog.req.LoginLogReq;
import com.wzkris.message.feign.userlog.req.OperateLogReq;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : RPC -- 系统日志服务
 * @date : 2023/3/13 16:12
 */
@FeignClient(name = ServiceIdConstant.MESSAGE, contextId = "UserLogFeign",
        fallbackFactory = UserLogFeignFallback.class,
        path = "/feign-user-log")
public interface UserLogFeign {

    /**
     * 新增操作日志
     */
    @PostMapping("/save-operlogs")
    void saveOperlogs(@RequestBody List<OperateLogReq> operateLogReqs);

    /**
     * 新增登录日志
     */
    @PostMapping("/save-loginlogs")
    void saveLoginlog(@RequestBody LoginLogReq loginLogReq);

}
