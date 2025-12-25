package com.wzkris.system.service;

import com.wzkris.system.domain.AdminOperateLogDO;
import com.wzkris.system.domain.req.adminlog.AdminOperateLogQueryReq;

import java.util.List;

/**
 * 操作日志 服务层
 *
 * @author wzkris
 */
public interface AdminOperateLogService {

    List<AdminOperateLogDO> list(AdminOperateLogQueryReq queryReq);

}
