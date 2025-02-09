package com.wzkris.system.api;

import com.wzkris.system.api.domain.request.LoginLogReq;
import com.wzkris.system.api.domain.request.OperLogReq;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : RPC -- 操作日志服务
 * @date : 2023/3/13 16:12
 */
public interface RemoteLogApi {

    /**
     * 新增操作日志
     */
    void insertOperlog(OperLogReq operLogReq);

    /**
     * 新增登录日志
     */
    void insertLoginlog(LoginLogReq loginLogReq);
}
