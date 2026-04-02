package com.wzkris.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wzkris.system.domain.AdminOperateLogDO;
import com.wzkris.system.mapper.AdminOperateLogMapper;
import com.wzkris.system.service.AdminOperateLogService;
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
        extends ServiceImpl<AdminOperateLogMapper, AdminOperateLogDO>
        implements AdminOperateLogService {

}

