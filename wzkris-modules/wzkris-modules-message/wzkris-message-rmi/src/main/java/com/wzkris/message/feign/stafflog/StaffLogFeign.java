package com.wzkris.message.feign.stafflog;

import com.wzkris.common.openfeign.constants.ServiceIdConstant;
import com.wzkris.message.feign.stafflog.fallback.StaffLogFeignFallback;
import com.wzkris.message.feign.stafflog.req.StaffLoginLogReq;
import com.wzkris.message.feign.stafflog.req.StaffOperateLogReq;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = ServiceIdConstant.MESSAGE, contextId = "StaffLogFeign",
        fallbackFactory = StaffLogFeignFallback.class,
        path = "/feign-staff-log")
public interface StaffLogFeign {

    /**
     * 新增操作日志
     */
    @PostMapping("/save-operlogs")
    void saveOperlogs(@RequestBody List<StaffOperateLogReq> staffOperateLogReqs);

    /**
     * 新增登录日志
     */
    @PostMapping("/save-loginlogs")
    void saveLoginlog(@RequestBody StaffLoginLogReq staffLoginLogReq);

}
