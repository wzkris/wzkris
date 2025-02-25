package com.wzkris.system.service;

import com.wzkris.system.domain.dto.SimpleMessageDTO;
import jakarta.annotation.Nonnull;

import java.util.List;

public interface SysNotifyService {

    /**
     * 发送通知
     *
     * @param toUsers    接收方用户ID
     * @param messageDTO 消息
     */
    boolean sendNotify(@Nonnull List<Long> toUsers, SimpleMessageDTO messageDTO);
}
