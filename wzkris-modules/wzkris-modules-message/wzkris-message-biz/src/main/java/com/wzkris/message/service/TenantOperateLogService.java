package com.wzkris.message.service;

import com.wzkris.message.domain.TenantOperateLogDO;
import com.wzkris.message.domain.req.tenantlog.TenantOperateLogQueryReq;
import com.wzkris.message.domain.vo.tenantlog.TenantOperateLogInfoVO;

import java.util.List;

/**
 * 操作日志 服务层
 *
 * @author wzkris
 */
public interface TenantOperateLogService {

    List<TenantOperateLogDO> list(TenantOperateLogQueryReq queryReq);

    List<TenantOperateLogInfoVO> listInfoVO(TenantOperateLogQueryReq queryReq);

}
