package com.wzkris.system.api;

import com.wzkris.common.core.constant.ApplicationNameConstants;
import com.wzkris.system.api.domain.request.LoginLogReq;
import com.wzkris.system.api.domain.request.OperLogReq;
import com.wzkris.system.api.fallback.RemoteLogApiFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import static com.wzkris.common.core.constant.SecurityConstants.INNER_NOAUTH_REQUEST_PATH;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : RPC -- 操作日志服务
 * @date : 2023/3/13 16:12
 */
@FeignClient(value = ApplicationNameConstants.SYSTEM, contextId = "RemoteLogApi", fallbackFactory = RemoteLogApiFallback.class)
public interface RemoteLogApi {

    /**
     * 新增操作日志
     */
    @PostMapping(INNER_NOAUTH_REQUEST_PATH + "/add_operlog")
    void insertOperlog(@RequestBody OperLogReq operLogReq);

    /**
     * 新增登录日志
     */
    @PostMapping(INNER_NOAUTH_REQUEST_PATH + "/add_loginlog")
    void insertLoginlog(@RequestBody LoginLogReq loginLogReq);
}
