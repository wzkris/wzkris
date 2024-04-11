package com.thingslink.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.thingslink.common.core.utils.StringUtil;
import com.thingslink.system.domain.SysLoginLog;
import com.thingslink.system.mapper.SysLoginLogMapper;
import com.thingslink.system.service.SysLoginLogService;
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
public class SysLoginLogServiceImpl implements SysLoginLogService {
    private final SysLoginLogMapper sysLoginLogMapper;

    @Override
    public List<SysLoginLog> list(SysLoginLog loginLog) {
        LambdaQueryWrapper<SysLoginLog> lqw = this.buildQueryWrapper(loginLog);
        return this.sysLoginLogMapper.selectList(lqw);
    }

    private LambdaQueryWrapper<SysLoginLog> buildQueryWrapper(SysLoginLog loginLog) {
        return new LambdaQueryWrapper<SysLoginLog>()
                .eq(StringUtil.isNotNull(loginLog.getUserId()), SysLoginLog::getUserId, loginLog.getUserId())
                .eq(StringUtil.isNotNull(loginLog.getIpAddr()), SysLoginLog::getIpAddr, loginLog.getIpAddr())
                .between(loginLog.getParams().get("beginTime") != null && loginLog.getParams().get("endTime") != null,
                        SysLoginLog::getLoginTime, loginLog.getParams().get("beginTime"), loginLog.getParams().get("endTime"))
                .orderByDesc(SysLoginLog::getLogId);
    }
}
