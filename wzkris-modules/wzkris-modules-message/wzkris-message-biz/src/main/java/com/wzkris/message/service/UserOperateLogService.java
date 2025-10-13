package com.wzkris.message.service;

import com.wzkris.message.domain.UserOperateLogDO;
import com.wzkris.message.domain.req.userlog.UserOperateLogQueryReq;

import java.util.List;

/**
 * 操作日志 服务层
 *
 * @author wzkris
 */
public interface UserOperateLogService {

    List<UserOperateLogDO> list(UserOperateLogQueryReq queryReq);

}
