package com.wzkris.system.service.impl;

import com.wzkris.system.mapper.AnnouncementInfoMapper;
import com.wzkris.system.service.AnnouncementInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 系统消息 服务层实现
 *
 * @author wzkris
 */
@Service
@RequiredArgsConstructor
public class AnnouncementInfoServiceImpl implements AnnouncementInfoService {

    private final AnnouncementInfoMapper announcementInfoMapper;

}
