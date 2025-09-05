package com.wzkris.system.rmi;

import com.wzkris.common.web.utils.BeanUtil;
import com.wzkris.system.domain.UserLoginLogDO;
import com.wzkris.system.domain.UserOperateLogDO;
import com.wzkris.system.mapper.UserLoginLogMapper;
import com.wzkris.system.mapper.UserOperateLogMapper;
import com.wzkris.system.rmi.domain.req.LoginLogReq;
import com.wzkris.system.rmi.domain.req.OperateLogReq;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Hidden
@RestController
@RequestMapping("/feign-user-log")
@RequiredArgsConstructor
public class UserLogFeignImpl implements UserLogFeign {

    private final UserOperateLogMapper userOperateLogMapper;

    private final UserLoginLogMapper userLoginLogMapper;

    @Override
    public void saveOperlogs(@RequestBody List<OperateLogReq> operateLogReqs) {
        List<UserOperateLogDO> operLogs = BeanUtil.convert(operateLogReqs, UserOperateLogDO.class);
        userOperateLogMapper.insert(operLogs, 1000);
    }

    @Override
    public void saveLoginlog(@RequestBody LoginLogReq loginLogReq) {
        UserLoginLogDO userLoginLogDO = BeanUtil.convert(loginLogReq, UserLoginLogDO.class);
        userLoginLogMapper.insert(userLoginLogDO);
    }

}
