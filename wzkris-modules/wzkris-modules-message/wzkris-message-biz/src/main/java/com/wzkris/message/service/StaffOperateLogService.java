package com.wzkris.message.service;

import com.wzkris.message.domain.StaffOperateLogDO;
import com.wzkris.message.domain.req.stafflog.StaffOperateLogQueryReq;
import com.wzkris.message.domain.req.userlog.UserOperateLogQueryReq;

import java.util.List;

/**
 * 操作日志 服务层
 *
 * @author wzkris
 */
public interface StaffOperateLogService {

    List<StaffOperateLogDO> list(StaffOperateLogQueryReq queryReq);

}
