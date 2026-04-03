package com.wzkris.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wzkris.system.domain.TenantOperateLogDO;
import com.wzkris.system.mapper.TenantOperateLogMapper;
import com.wzkris.system.service.TenantOperateLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TenantOperateLogServiceImpl
        extends ServiceImpl<TenantOperateLogMapper, TenantOperateLogDO>
        implements TenantOperateLogService {

}
