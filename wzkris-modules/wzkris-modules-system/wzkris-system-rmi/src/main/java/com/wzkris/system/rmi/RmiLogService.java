package com.wzkris.system.rmi;

import com.wzkris.system.rmi.domain.req.LoginLogReq;
import com.wzkris.system.rmi.domain.req.OperLogReq;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : RPC -- 操作日志服务
 * @date : 2023/3/13 16:12
 */
public interface RmiLogService {

    /**
     * 新增操作日志
     */
    void saveOperlog(OperLogReq operLogReq);

    /**
     * 新增登录日志
     */
    void saveLoginlog(LoginLogReq loginLogReq);
}
