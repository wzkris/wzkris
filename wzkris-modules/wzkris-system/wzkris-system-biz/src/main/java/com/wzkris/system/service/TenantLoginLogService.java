package com.wzkris.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wzkris.system.domain.TenantLoginLogDO;
import com.wzkris.system.request.tenantlog.TenantLoginLogQueryRequest;

import java.util.List;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 登录日志
 * @date : 2024/1/10 13:55
 */
public interface TenantLoginLogService extends IService<TenantLoginLogDO> {

    List<TenantLoginLogDO> list(TenantLoginLogQueryRequest request);

}

