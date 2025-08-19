package com.wzkris.system.rmi;

import com.wzkris.common.openfeign.constants.ServiceIdConstant;
import com.wzkris.common.openfeign.core.RmiFeign;
import com.wzkris.system.rmi.domain.req.LoginLogReq;
import com.wzkris.system.rmi.domain.req.OperLogReq;
import com.wzkris.system.rmi.fallback.SysLogFeignFallback;
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
@FeignClient(name = ServiceIdConstant.SYSTEM, contextId = "SysLogFeign",
        fallbackFactory = SysLogFeignFallback.class, path = "/feign-sys-log")
public interface SysLogFeign extends RmiFeign {

    /**
     * 新增操作日志
     */
    @PostMapping("/save-operlogs")
    void saveOperlogs(@RequestBody List<OperLogReq> operLogReqs);

    /**
     * 新增登录日志
     */
    @PostMapping("/save-loginlogs")
    void saveLoginlog(@RequestBody LoginLogReq loginLogReq);

}
