package com.wzkris.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wzkris.system.domain.AdminOperateLogDO;
import com.wzkris.system.request.adminlog.AdminOperateLogQueryRequest;

import java.util.List;

/**
 * 操作日志 服务层
 *
 * @author wzkris
 */
public interface AdminOperateLogService extends IService<AdminOperateLogDO> {

    List<AdminOperateLogDO> list(AdminOperateLogQueryRequest request);

}

