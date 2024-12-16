package com.wzkris.system.service;

import com.wzkris.system.domain.SysNotify;

import java.util.List;

public interface SysNotifyService {

    /**
     * 发送通知
     *
     * @param userIds 接收方用户ID
     * @param notify  通知
     */
    boolean sendNotify(List<Long> userIds, SysNotify notify);
}
