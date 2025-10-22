package com.wzkris.message.feign.userlog;

import com.wzkris.common.web.utils.BeanUtil;
import com.wzkris.message.domain.UserLoginLogDO;
import com.wzkris.message.domain.UserOperateLogDO;
import com.wzkris.message.feign.userlog.req.UserLoginLogReq;
import com.wzkris.message.feign.userlog.req.UserOperateLogReq;
import com.wzkris.message.mapper.UserLoginLogMapper;
import com.wzkris.message.mapper.UserOperateLogMapper;
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
    public void saveOperlogs(@RequestBody List<UserOperateLogReq> userOperateLogReqs) {
        List<UserOperateLogDO> operLogs = BeanUtil.convert(userOperateLogReqs, UserOperateLogDO.class);
        userOperateLogMapper.insert(operLogs, 1000);
    }

    @Override
    public void saveLoginlog(@RequestBody UserLoginLogReq userLoginLogReq) {
        UserLoginLogDO userLoginLogDO = BeanUtil.convert(userLoginLogReq, UserLoginLogDO.class);
        userLoginLogMapper.insert(userLoginLogDO);
    }

}
