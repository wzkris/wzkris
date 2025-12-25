package com.wzkris.system.service;

import com.wzkris.system.domain.AdminLoginLogDO;
import com.wzkris.system.domain.req.adminlog.AdminLoginLogQueryReq;

import java.util.List;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 登录日志
 * @date : 2024/1/10 13:55
 */
public interface AdminLoginLogService {

    List<AdminLoginLogDO> list(AdminLoginLogQueryReq queryReq);

}
