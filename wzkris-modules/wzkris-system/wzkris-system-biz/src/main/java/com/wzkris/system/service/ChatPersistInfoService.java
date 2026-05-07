package com.wzkris.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wzkris.system.domain.ChatPersistInfoDO;
import com.wzkris.system.enums.chat.MediaFormatEnum;
import com.wzkris.system.enums.chat.ResourceTypeEnum;

public interface ChatPersistInfoService extends IService<ChatPersistInfoDO> {

    void persistOutbound(long senderId, long receiverId, ResourceTypeEnum messageType,
                         MediaFormatEnum mediaFormat, byte[] content);

}
