package com.wzkris.system.rmi;

import com.wzkris.common.core.utils.BeanUtil;
import com.wzkris.system.domain.SysLoginLog;
import com.wzkris.system.domain.SysOperLog;
import com.wzkris.system.mapper.SysLoginLogMapper;
import com.wzkris.system.mapper.SysOperLogMapper;
import com.wzkris.system.rmi.domain.req.LoginLogReq;
import com.wzkris.system.rmi.domain.req.OperLogReq;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 操作日志RPC
 * @date : 2023/3/13 16:13
 */
@RestController
@RequiredArgsConstructor
public class RmiLogFeignImpl implements RmiLogFeign {

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
