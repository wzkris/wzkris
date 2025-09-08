package com.wzkris.system.service;

import com.wzkris.system.domain.UserOperateLogDO;
import com.wzkris.system.domain.req.userlog.UserOperateLogQueryReq;

import java.util.List;

/**
 * 操作日志 服务层
 *
 * @author wzkris
 */
public interface UserOperateLogService {

    List<UserOperateLogDO> list(UserOperateLogQueryReq queryReq);

}
