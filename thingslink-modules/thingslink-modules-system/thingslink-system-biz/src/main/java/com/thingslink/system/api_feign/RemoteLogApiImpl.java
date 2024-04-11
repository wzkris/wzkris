package com.thingslink.system.api_feign;

import com.thingslink.common.security.annotation.InnerAuth;
import com.thingslink.system.api.RemoteLogApi;
import com.thingslink.system.api.domain.LoginLogDTO;
import com.thingslink.system.api.domain.OperLogDTO;
import com.thingslink.system.domain.LoginLog;
import com.thingslink.system.domain.OperLog;
import com.thingslink.system.mapper.LoginLogMapper;
import com.thingslink.system.mapper.OperLogMapper;
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
    private final OperLogMapper operLogMapper;
    private final LoginLogMapper loginLogMapper;

    @Override
    public void insertOperlog(@RequestBody OperLogDTO operLogDTO) {
        OperLog operLog = new OperLog();
        BeanUtils.copyProperties(operLogDTO, operLog);
        operLogMapper.insert(operLog);
    }

    @Override
    public void insertLoginlog(@RequestBody LoginLogDTO loginLogDTO) {
        LoginLog loginLog = new LoginLog();
        BeanUtils.copyProperties(loginLogDTO, loginLog);
        loginLogMapper.insert(loginLog);
    }
}
