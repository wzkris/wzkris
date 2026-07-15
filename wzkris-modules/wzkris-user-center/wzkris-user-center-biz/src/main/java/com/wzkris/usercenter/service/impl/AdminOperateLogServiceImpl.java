package com.wzkris.usercenter.service.impl;

import com.wzkris.common.orm.plus.ServiceImplPlus;
import com.wzkris.usercenter.domain.AdminOperateLogDO;
import com.wzkris.usercenter.mapper.AdminOperateLogMapper;
import com.wzkris.usercenter.service.AdminOperateLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 操作日志 服务层处理
 *
 * @author wzkris
 */
@Service
@RequiredArgsConstructor
public class AdminOperateLogServiceImpl
        extends ServiceImplPlus<AdminOperateLogMapper, AdminOperateLogDO>
        implements AdminOperateLogService {

}
