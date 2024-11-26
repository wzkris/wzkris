package com.wzkris.system.service.impl;

import com.wzkris.system.mapper.SysNotifyMapper;
import com.wzkris.system.service.SysNotifyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 通知公告 服务层实现
 *
 * @author wzkris
 */
@Service
@RequiredArgsConstructor
public class SysNotifyServiceImpl implements SysNotifyService {
    private final SysNotifyMapper noticeMapper;

}
