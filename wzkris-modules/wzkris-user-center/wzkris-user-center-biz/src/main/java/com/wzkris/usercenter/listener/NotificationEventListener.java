package com.wzkris.usercenter.listener;

import com.wzkris.common.core.enums.AuthTypeEnum;
import com.wzkris.common.core.utils.ResultUtil;
import com.wzkris.usercenter.event.CreateAdminEvent;
import com.wzkris.usercenter.event.CreateMemberEvent;
import com.wzkris.usercenter.event.CreateTenantEvent;
import com.wzkris.usercenter.remote.api.notification.NotificationInfoRemoteApi;
import com.wzkris.usercenter.remote.api.notification.request.NotificationRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Collections;

/**
 * 通知 事件监听器
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationEventListener {

    private final NotificationInfoRemoteApi notificationInfoRemote;

    @Async
    @EventListener
    public void createTenantEvent(CreateTenantEvent event) {
        NotificationRequest request = new NotificationRequest(
                Collections.singletonList(event.getReceiverId()), AuthTypeEnum.ADMIN,
                "租户创建成功",
                String.format(
                        "租户：%s创建成功，超级管理员账号：%s，临时登录密码：%s，临时操作密码：%s",
                        event.getTenantName(),
                        event.getUsername(),
                        event.getLoginPwd(),
                        event.getOperPwd()));

        if (!ResultUtil.checkNoData(notificationInfoRemote.send2Users(request))) {
            log.warn("发送租户创建通知失败: {}", request);
        }
    }

    @Async
    @EventListener
    public void createAdminEvent(CreateAdminEvent event) {
        NotificationRequest request = new NotificationRequest(
                Collections.singletonList(event.getReceiverId()), AuthTypeEnum.ADMIN,
                "管理员创建成功",
                String.format("管理员账号：%s创建成功，临时登录密码：%s", event.getUsername(), event.getPassword()));

        if (!ResultUtil.checkNoData(notificationInfoRemote.send2Users(request))) {
            log.warn("发送管理员创建通知失败: {}", request);
        }
    }

    @Async
    @EventListener
    public void createMemberEvent(CreateMemberEvent event) {
        NotificationRequest request = new NotificationRequest(
                Collections.singletonList(event.getReceiverId()), AuthTypeEnum.TENANT,
                "租户账号创建成功",
                String.format("租户账号：%s创建成功，临时登录密码：%s", event.getUsername(), event.getPassword()));

        if (!ResultUtil.checkNoData(notificationInfoRemote.send2Users(request))) {
            log.warn("发送租户账号创建通知失败: {}", request);
        }
    }

}

