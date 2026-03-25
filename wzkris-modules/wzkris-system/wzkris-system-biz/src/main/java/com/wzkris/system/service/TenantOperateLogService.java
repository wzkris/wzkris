package com.wzkris.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wzkris.system.domain.TenantOperateLogDO;
import com.wzkris.system.request.tenantlog.TenantOperateLogQueryRequest;
import com.wzkris.system.response.tenantlog.TenantOperateLogInfoResponse;

import java.util.List;

/**
 * 操作日志 服务层
 *
 * @author wzkris
 */
public interface TenantOperateLogService extends IService<TenantOperateLogDO> {

    List<TenantOperateLogDO> list(TenantOperateLogQueryRequest QueryRequest);

    List<TenantOperateLogInfoResponse> listInfoVO(TenantOperateLogQueryRequest QueryRequest);

}

