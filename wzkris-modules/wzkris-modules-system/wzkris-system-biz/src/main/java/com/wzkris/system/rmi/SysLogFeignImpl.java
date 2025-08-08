package com.wzkris.system.rmi;

import com.wzkris.common.web.utils.BeanUtil;
import com.wzkris.system.domain.SysLoginLog;
import com.wzkris.system.domain.SysOperLog;
import com.wzkris.system.mapper.SysLoginLogMapper;
import com.wzkris.system.mapper.SysOperLogMapper;
import com.wzkris.system.rmi.domain.req.LoginLogReq;
import com.wzkris.system.rmi.domain.req.OperLogReq;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Hidden
@RestController
@RequiredArgsConstructor
public class SysLogFeignImpl implements SysLogFeign {

    private final SysOperLogMapper operLogMapper;

    private final SysLoginLogMapper loginLogMapper;

    @Override
    public void saveOperlogs(@RequestBody List<OperLogReq> operLogReqs) {
        List<SysOperLog> operLogs = BeanUtil.convert(operLogReqs, SysOperLog.class);
        operLogMapper.insert(operLogs, 1000);
    }

    @Override
    public void saveLoginlog(@RequestBody LoginLogReq loginLogReq) {
        SysLoginLog sysLoginLog = BeanUtil.convert(loginLogReq, SysLoginLog.class);
        loginLogMapper.insert(sysLoginLog);
    }

}
