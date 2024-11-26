package com.wzkris.system.service.impl;

import com.wzkris.system.mapper.SysOperLogMapper;
import com.wzkris.system.service.SysOperLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 操作日志 服务层处理
 *
 * @author wzkris
 */
@Service
@RequiredArgsConstructor
public class SysOperLogServiceImpl implements SysOperLogService {
    private final SysOperLogMapper operLogMapper;

}
