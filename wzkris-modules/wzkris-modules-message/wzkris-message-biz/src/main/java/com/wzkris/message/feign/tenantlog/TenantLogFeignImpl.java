package com.wzkris.message.feign.tenantlog;

import com.wzkris.common.web.utils.BeanUtil;
import com.wzkris.message.domain.TenantLoginLogDO;
import com.wzkris.message.domain.TenantOperateLogDO;
import com.wzkris.message.feign.tenantlog.req.LoginLogReq;
import com.wzkris.message.feign.tenantlog.req.OperateLogReq;
import com.wzkris.message.mapper.TenantLoginLogMapper;
import com.wzkris.message.mapper.TenantOperateLogMapper;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Hidden
@RestController
@RequestMapping("/feign-tenant-log")
@RequiredArgsConstructor
public class TenantLogFeignImpl implements TenantLogFeign {

    private final TenantOperateLogMapper operateLogMapper;

    private final TenantLoginLogMapper loginLogMapper;

    @Override
    public void saveOperlogs(@RequestBody List<OperateLogReq> operateLogReqs) {
        List<TenantOperateLogDO> operLogs = BeanUtil.convert(operateLogReqs, TenantOperateLogDO.class);
        operateLogMapper.insert(operLogs, 1000);
    }

    @Override
    public void saveLoginlog(@RequestBody LoginLogReq loginLogReq) {
        TenantLoginLogDO loginLogDO = BeanUtil.convert(loginLogReq, TenantLoginLogDO.class);
        loginLogMapper.insert(loginLogDO);
    }

}
