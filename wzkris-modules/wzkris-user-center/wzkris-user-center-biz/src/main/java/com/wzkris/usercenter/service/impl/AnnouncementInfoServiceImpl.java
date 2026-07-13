package com.wzkris.usercenter.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
        extends ServiceImpl<AnnouncementInfoMapper, AnnouncementInfoDO>
        implements AnnouncementInfoService {

}
