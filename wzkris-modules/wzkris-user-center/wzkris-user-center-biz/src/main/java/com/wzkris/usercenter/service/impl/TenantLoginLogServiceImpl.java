package com.wzkris.usercenter.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wzkris.usercenter.domain.TenantLoginLogDO;
import com.wzkris.usercenter.mapper.TenantLoginLogMapper;
import com.wzkris.usercenter.service.TenantLoginLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TenantLoginLogServiceImpl
        extends ServiceImpl<TenantLoginLogMapper, TenantLoginLogDO>
        implements TenantLoginLogService {

}
