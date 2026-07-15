package com.wzkris.usercenter.service.impl;

import com.wzkris.common.orm.plus.ServiceImplPlus;
import com.wzkris.usercenter.domain.TenantLoginLogDO;
import com.wzkris.usercenter.mapper.TenantLoginLogMapper;
import com.wzkris.usercenter.service.TenantLoginLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TenantLoginLogServiceImpl
        extends ServiceImplPlus<TenantLoginLogMapper, TenantLoginLogDO>
        implements TenantLoginLogService {

}
