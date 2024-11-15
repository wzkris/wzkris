package com.wzkris.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.system.domain.SysLoginLog;
import com.wzkris.system.mapper.SysLoginLogMapper;
import com.wzkris.system.service.SysLoginLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 登录日志
 * @date : 2024/1/10 13:55
 */
@Service
@RequiredArgsConstructor
public class SysLoginLogServiceImpl implements SysLoginLogService {
    private final SysLoginLogMapper loginLogMapper;

    @Override
    public List<SysLoginLog> list(SysLoginLog loginLog) {
        LambdaQueryWrapper<SysLoginLog> lqw = this.buildQueryWrapper(loginLog);
        return this.loginLogMapper.selectList(lqw);
    }

    private LambdaQueryWrapper<SysLoginLog> buildQueryWrapper(SysLoginLog loginLog) {
        return new LambdaQueryWrapper<SysLoginLog>()
                .eq(StringUtil.isNotNull(loginLog.getTenantId()), SysLoginLog::getTenantId, loginLog.getTenantId())
                .eq(StringUtil.isNotNull(loginLog.getStatus()), SysLoginLog::getStatus, loginLog.getStatus())
                .like(StringUtil.isNotNull(loginLog.getUsername()), SysLoginLog::getUsername, loginLog.getUsername())
                .like(StringUtil.isNotNull(loginLog.getLoginIp()), SysLoginLog::getLoginIp, loginLog.getLoginIp())
                .between(loginLog.getParams().get("beginTime") != null && loginLog.getParams().get("endTime") != null,
                        SysLoginLog::getLoginTime, loginLog.getParams().get("beginTime"), loginLog.getParams().get("endTime"))
                .orderByDesc(SysLoginLog::getLogId);
    }
}
