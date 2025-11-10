package com.wzkris.message.feign.tenantlog;

import com.wzkris.common.openfeign.constants.ServiceIdConstant;
import com.wzkris.message.feign.tenantlog.fallback.TenantLogFeignFallback;
import com.wzkris.message.feign.tenantlog.req.LoginLogReq;
import com.wzkris.message.feign.tenantlog.req.OperateLogReq;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = ServiceIdConstant.MESSAGE, contextId = "TenantLogFeign",
        fallbackFactory = TenantLogFeignFallback.class,
        path = "/feign-tenant-log")
public interface TenantLogFeign {

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
