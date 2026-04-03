package com.wzkris.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wzkris.system.domain.TenantLoginLogDO;
import com.wzkris.system.mapper.TenantLoginLogMapper;
import com.wzkris.system.service.TenantLoginLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TenantLoginLogServiceImpl
        extends ServiceImpl<TenantLoginLogMapper, TenantLoginLogDO>
        implements TenantLoginLogService {

}
