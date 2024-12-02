package com.wzkris.system.api;

import com.wzkris.common.core.utils.MapstructUtil;
import com.wzkris.common.openfeign.annotation.InnerAuth;
import com.wzkris.system.api.domain.request.LoginLogReq;
import com.wzkris.system.api.domain.request.OperLogReq;
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
    public void insertOperlog(@RequestBody OperLogReq operLogReq) {
        SysOperLog sysOperLog = MapstructUtil.convert(operLogReq, SysOperLog.class);
        operLogMapper.insert(sysOperLog);
    }

    @Override
    public void insertLoginlog(@RequestBody LoginLogReq loginLogReq) {
        SysLoginLog sysLoginLog = MapstructUtil.convert(loginLogReq, SysLoginLog.class);
        loginLogMapper.insert(sysLoginLog);
    }
}
