package com.wzkris.system.service;

import java.util.Collections;
import java.util.List;

/**
 * 系统消息 服务层
 *
 * @author wzkris
 */
public interface SysMessageService {

    /**
     * 发送通知
     *
     * @param userIds 接收方用户ID
     * @param msgId   通知ID
     */
    boolean sendNotify(List<Long> userIds, Long msgId);

    /**
     * 校验是否为通知
     *
     * @param msgId 消息ID
     */
    boolean checkIsNotify(Long msgId);

    /**
     * 校验是否为草稿
     *
     * @param msgId 消息ID
     */
    boolean checkIsDraft(List<Long> msgId);

    default boolean checkIsDraft(Long msgId) {
        return this.checkIsDraft(Collections.singletonList(msgId));
    }

}
