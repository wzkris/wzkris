package com.wzkris.usercenter.service.impl;

import com.wzkris.common.orm.plus.ServiceImplPlus;
import com.wzkris.usercenter.domain.AnnouncementInfoDO;
import com.wzkris.usercenter.mapper.AnnouncementInfoMapper;
import com.wzkris.usercenter.service.AnnouncementInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 系统消息 服务层实现
 *
 * @author wzkris
 */
@Service
@RequiredArgsConstructor
public class AnnouncementInfoServiceImpl
        extends ServiceImplPlus<AnnouncementInfoMapper, AnnouncementInfoDO>
        implements AnnouncementInfoService {

}
