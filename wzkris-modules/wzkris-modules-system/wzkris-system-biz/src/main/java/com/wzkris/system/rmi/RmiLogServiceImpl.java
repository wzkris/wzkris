package com.wzkris.system.rmi;

import com.wzkris.common.core.utils.BeanUtil;
import com.wzkris.system.domain.SysLoginLog;
import com.wzkris.system.domain.SysOperLog;
import com.wzkris.system.mapper.SysLoginLogMapper;
import com.wzkris.system.mapper.SysOperLogMapper;
import com.wzkris.system.rmi.domain.req.LoginLogReq;
import com.wzkris.system.rmi.domain.req.OperLogReq;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 操作日志RPC
 * @date : 2023/3/13 16:13
 */
@Service
@DubboService
@RequiredArgsConstructor
public class RmiLogServiceImpl implements RmiLogService {

    private final SysOperLogMapper operLogMapper;

    private final SysLoginLogMapper loginLogMapper;

    @Override
    public void saveOperlog(@RequestBody OperLogReq operLogReq) {
        SysOperLog sysOperLog = BeanUtil.convert(operLogReq, SysOperLog.class);
        operLogMapper.insert(sysOperLog);
    }

    @Override
    public void saveLoginlog(@RequestBody LoginLogReq loginLogReq) {
        SysLoginLog sysLoginLog = BeanUtil.convert(loginLogReq, SysLoginLog.class);
        loginLogMapper.insert(sysLoginLog);
    }
}
