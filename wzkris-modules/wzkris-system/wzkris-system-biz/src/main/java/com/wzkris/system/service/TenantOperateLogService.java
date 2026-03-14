package com.wzkris.system.service;

import com.wzkris.system.domain.TenantOperateLogDO;
import com.wzkris.system.domain.req.tenantlog.TenantOperateLogQueryReq;
import com.wzkris.system.domain.resp.tenantlog.TenantOperateLogInfoResp;

import java.util.List;

/**
 * 操作日志 服务层
 *
 * @author wzkris
 */
public interface TenantOperateLogService {

    List<TenantOperateLogDO> list(TenantOperateLogQueryReq queryReq);

    List<TenantOperateLogInfoResp> listInfoVO(TenantOperateLogQueryReq queryReq);

}
