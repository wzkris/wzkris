package com.thingslink.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.thingslink.common.core.utils.StringUtil;
import com.thingslink.system.domain.LoginLog;
import com.thingslink.system.mapper.LoginLogMapper;
import com.thingslink.system.service.LoginLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : dl
 * @date : 2024/1/10 13:55
 */
@Service
@RequiredArgsConstructor
public class LoginLogServiceImpl implements LoginLogService {
    private final LoginLogMapper loginLogMapper;

    @Override
    public List<LoginLog> list(LoginLog loginLog) {
        LambdaQueryWrapper<LoginLog> lqw = this.buildQueryWrapper(loginLog);
        return this.loginLogMapper.selectList(lqw);
    }

    private LambdaQueryWrapper<LoginLog> buildQueryWrapper(LoginLog loginLog) {
        return new LambdaQueryWrapper<LoginLog>()
                .eq(StringUtil.isNotNull(loginLog.getUserId()), LoginLog::getUserId, loginLog.getUserId())
                .eq(StringUtil.isNotNull(loginLog.getIpAddr()), LoginLog::getIpAddr, loginLog.getIpAddr())
                .between(loginLog.getParams().get("beginTime") != null && loginLog.getParams().get("endTime") != null,
                        LoginLog::getLoginTime, loginLog.getParams().get("beginTime"), loginLog.getParams().get("endTime"))
                .orderByDesc(LoginLog::getLogId);
    }
}
