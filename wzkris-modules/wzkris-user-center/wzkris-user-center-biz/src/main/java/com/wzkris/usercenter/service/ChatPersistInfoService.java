package com.wzkris.usercenter.service;

import com.wzkris.common.orm.plus.IServicePlus;
import com.wzkris.usercenter.domain.ChatPersistInfoDO;
import com.wzkris.usercenter.enums.chat.MediaFormatEnum;
import com.wzkris.usercenter.enums.chat.ResourceTypeEnum;

public interface ChatPersistInfoService extends IServicePlus<ChatPersistInfoDO> {

    void persistOutbound(long senderId, long receiverId, ResourceTypeEnum messageType,
                         MediaFormatEnum mediaFormat, byte[] content);

}
