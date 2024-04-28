package com.thingslink.system.api;

import com.thingslink.system.api.domain.LoginLogDTO;
import com.thingslink.system.api.domain.OperLogDTO;
import com.thingslink.system.api.fallback.RemoteLogFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import static com.thingslink.common.core.constant.SecurityConstants.INNER_REQUEST_PATH;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : RPC -- 操作日志服务
 * @date : 2023/3/13 16:12
 */
@FeignClient(value = "thingslink-system", contextId = "RemoteLogApi", fallbackFactory = RemoteLogFallback.class)
public interface RemoteLogApi {

    /**
     * 新增操作日志
     */
    @PostMapping(INNER_REQUEST_PATH + "/operlog")
    void insertOperlog(@RequestBody OperLogDTO operLogDTO);

    /**
     * 新增登录日志
     */
    @PostMapping(INNER_REQUEST_PATH + "/loginlog")
    void insertLoginlog(@RequestBody LoginLogDTO loginLogDTO);
}
