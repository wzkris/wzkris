package com.wzkris.system.service;

import com.wzkris.system.domain.UserLoginLogDO;
import com.wzkris.system.domain.req.UserLoginLogQueryReq;

import java.util.List;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 登录日志
 * @date : 2024/1/10 13:55
 */
public interface UserLoginLogService {

    List<UserLoginLogDO> list(UserLoginLogQueryReq queryReq);

}
