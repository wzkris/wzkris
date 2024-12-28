package com.wzkris.system.service;

import java.util.List;

/**
 * 系统消息 服务层
 *
 * @author wzkris
 */
public interface SysMessageService {

    /**
     * 校验是否为系统公告
     *
     * @param msgId 消息ID
     */
    boolean checkIsSystem(Long msgId);

    /**
     * 校验是否为APP公告
     *
     * @param msgId 消息ID
     */
    boolean checkIsApp(Long msgId);

    /**
     * 校验是否为草稿
     *
     * @param msgId 消息ID
     */
    boolean checkIsDraft(Long msgId);

    /**
     * 校验是否为关闭
     *
     * @param msgIds 消息ID
     */
    boolean checkIsClose(List<Long> msgIds);
}
