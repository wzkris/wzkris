package com.wzkris.system.service.impl;

import com.wzkris.system.mapper.SysMessageMapper;
import com.wzkris.system.service.SysMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 系统消息 服务层实现
 *
 * @author wzkris
 */
@Service
@RequiredArgsConstructor
public class SysMessageServiceImpl implements SysMessageService {

    private final SysMessageMapper messageMapper;
}
