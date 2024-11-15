package com.wzkris.system.api;

import com.wzkris.common.core.utils.MapstructUtil;
import com.wzkris.common.openfeign.annotation.InnerAuth;
import com.wzkris.system.api.domain.LoginLogDTO;
import com.wzkris.system.api.domain.OperLogDTO;
import com.wzkris.system.domain.SysLoginLog;
import com.wzkris.system.domain.SysOperLog;
import com.wzkris.system.mapper.SysLoginLogMapper;
import com.wzkris.system.mapper.SysOperLogMapper;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 操作日志RPC
 * @date : 2023/3/13 16:13
 */
@Hidden
@InnerAuth
@RestController
@RequiredArgsConstructor
public class RemoteLogApiImpl implements RemoteLogApi {
    private final SysOperLogMapper operLogMapper;
    private final SysLoginLogMapper loginLogMapper;

    @Override
    public void insertOperlog(@RequestBody OperLogDTO operLogDTO) {
        SysOperLog sysOperLog = MapstructUtil.convert(operLogDTO, SysOperLog.class);
        operLogMapper.insert(sysOperLog);
    }

    @Override
    public void insertLoginlog(@RequestBody LoginLogDTO loginLogDTO) {
        SysLoginLog sysLoginLog = MapstructUtil.convert(loginLogDTO, SysLoginLog.class);
        loginLogMapper.insert(sysLoginLog);
    }
}
