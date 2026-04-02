package com.wzkris.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.system.domain.TenantOperateLogDO;
import com.wzkris.system.mapper.TenantOperateLogMapper;
import com.wzkris.system.request.tenantlog.TenantOperateLogMngQueryRequest;
import com.wzkris.system.response.tenantlog.TenantOperateLogInfoResponse;
import com.wzkris.system.service.TenantOperateLogService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TenantOperateLogServiceImpl
        extends ServiceImpl<TenantOperateLogMapper, TenantOperateLogDO>
        implements TenantOperateLogService {


}

