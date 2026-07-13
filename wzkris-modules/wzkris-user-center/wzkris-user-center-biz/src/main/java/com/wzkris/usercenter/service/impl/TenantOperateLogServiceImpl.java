package com.wzkris.usercenter.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wzkris.usercenter.domain.TenantOperateLogDO;
import com.wzkris.usercenter.mapper.TenantOperateLogMapper;
import com.wzkris.usercenter.service.TenantOperateLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TenantOperateLogServiceImpl
        extends ServiceImpl<TenantOperateLogMapper, TenantOperateLogDO>
        implements TenantOperateLogService {

}
