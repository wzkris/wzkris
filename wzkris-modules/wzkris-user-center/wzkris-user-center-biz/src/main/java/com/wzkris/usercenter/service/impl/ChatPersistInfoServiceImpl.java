package com.wzkris.usercenter.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wzkris.usercenter.domain.ChatPersistInfoDO;
import com.wzkris.usercenter.enums.chat.MediaFormatEnum;
import com.wzkris.usercenter.enums.chat.ResourceTypeEnum;
import com.wzkris.usercenter.mapper.ChatPersistInfoMapper;
import com.wzkris.usercenter.service.ChatPersistInfoService;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
public class ChatPersistInfoServiceImpl extends ServiceImpl<ChatPersistInfoMapper, ChatPersistInfoDO>
        implements ChatPersistInfoService {

    @Override
    public void persistOutbound(long senderId, long receiverId, ResourceTypeEnum messageType,
                                MediaFormatEnum mediaFormat, byte[] content) {
        OffsetDateTime now = OffsetDateTime.now();
        ChatPersistInfoDO row = new ChatPersistInfoDO();
        row.setSenderId(senderId);
        row.setReceiverId(receiverId);
        row.setSendTime(now);
        row.setReceiveTime(null);
        row.setRead(false);
        row.setResourceType(messageType);
        row.setMediaFormat(mediaFormat);
        row.setContent(content);
        save(row);
    }

}
