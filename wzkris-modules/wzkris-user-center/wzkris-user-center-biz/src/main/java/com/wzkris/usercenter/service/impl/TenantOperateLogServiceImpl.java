package com.wzkris.usercenter.service.impl;

import com.wzkris.common.orm.plus.ServiceImplPlus;
import com.wzkris.usercenter.domain.TenantOperateLogDO;
import com.wzkris.usercenter.mapper.TenantOperateLogMapper;
import com.wzkris.usercenter.service.TenantOperateLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TenantOperateLogServiceImpl
        extends ServiceImplPlus<TenantOperateLogMapper, TenantOperateLogDO>
        implements TenantOperateLogService {

}
