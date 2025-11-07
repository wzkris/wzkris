package com.wzkris.message.service;

import com.wzkris.message.domain.AdminOperateLogDO;
import com.wzkris.message.domain.req.userlog.AdminOperateLogQueryReq;

import java.util.List;

/**
 * 操作日志 服务层
 *
 * @author wzkris
 */
public interface AdminOperateLogService {

    List<AdminOperateLogDO> list(AdminOperateLogQueryReq queryReq);

}
