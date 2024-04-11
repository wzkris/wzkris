package com.thingslink.system.api_feign;

import com.thingslink.common.security.annotation.InnerAuth;
import com.thingslink.system.api.RemoteLogApi;
import com.thingslink.system.api.domain.LoginLogDTO;
import com.thingslink.system.api.domain.OperLogDTO;
import com.thingslink.system.domain.SysLoginLog;
import com.thingslink.system.domain.SysOperLog;
import com.thingslink.system.mapper.SysLoginLogMapper;
import com.thingslink.system.mapper.SysOperLogMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 操作日志RPC
 * @date : 2023/3/13 16:13
 */
@InnerAuth
@RestController
@RequiredArgsConstructor
public class RemoteLogApiImpl implements RemoteLogApi {
    private final SysOperLogMapper sysOperLogMapper;
    private final SysLoginLogMapper sysLoginLogMapper;

    @Override
    public void insertOperlog(@RequestBody OperLogDTO operLogDTO) {
        SysOperLog sysOperLog = new SysOperLog();
        BeanUtils.copyProperties(operLogDTO, sysOperLog);
        sysOperLogMapper.insert(sysOperLog);
    }

    @Override
    public void insertLoginlog(@RequestBody LoginLogDTO loginLogDTO) {
        SysLoginLog loginLog = new SysLoginLog();
        BeanUtils.copyProperties(loginLogDTO, loginLog);
        sysLoginLogMapper.insert(loginLog);
    }
}
