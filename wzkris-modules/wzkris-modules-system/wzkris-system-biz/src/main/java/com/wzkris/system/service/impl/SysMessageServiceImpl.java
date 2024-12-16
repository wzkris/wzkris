package com.wzkris.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wzkris.system.constant.MessageConstants;
import com.wzkris.system.domain.SysMessage;
import com.wzkris.system.mapper.SysMessageMapper;
import com.wzkris.system.service.SysMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 系统消息 服务层实现
 *
 * @author wzkris
 */
@Service
@RequiredArgsConstructor
public class SysMessageServiceImpl implements SysMessageService {
    private final SysMessageMapper messageMapper;

    @Override
    public boolean checkIsSystem(Long msgId) {
        LambdaQueryWrapper<SysMessage> lqw = Wrappers.lambdaQuery(SysMessage.class)
                .eq(SysMessage::getMsgId, msgId)
                .eq(SysMessage::getMsgType, MessageConstants.TYPE_SYSTEM);
        return messageMapper.exists(lqw);
    }

    @Override
    public boolean checkIsApp(Long msgId) {
        LambdaQueryWrapper<SysMessage> lqw = Wrappers.lambdaQuery(SysMessage.class)
                .eq(SysMessage::getMsgId, msgId)
                .eq(SysMessage::getMsgType, MessageConstants.TYPE_APP);
        return messageMapper.exists(lqw);
    }

    @Override
    public boolean checkIsDraft(Long msgId) {
        LambdaQueryWrapper<SysMessage> lqw = Wrappers.lambdaQuery(SysMessage.class)
                .eq(SysMessage::getMsgId, msgId)
                .eq(SysMessage::getStatus, MessageConstants.STATUS_DRAFT);
        return messageMapper.exists(lqw);
    }

    @Override
    public boolean checkIsClose(List<Long> msgIds) {
        LambdaQueryWrapper<SysMessage> lqw = Wrappers.lambdaQuery(SysMessage.class)
                .in(SysMessage::getMsgId, msgIds)
                .eq(SysMessage::getStatus, MessageConstants.STATUS_CLOSED);
        return messageMapper.selectCount(lqw) == msgIds.size();
    }
}
