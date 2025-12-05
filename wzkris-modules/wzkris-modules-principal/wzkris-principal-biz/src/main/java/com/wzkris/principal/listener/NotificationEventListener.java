package com.wzkris.principal.listener;

import com.wzkris.common.core.enums.AuthTypeEnum;
import com.wzkris.message.httpservice.notification.NotificationInfoHttpService;
import com.wzkris.message.httpservice.notification.req.NotificationReq;
import com.wzkris.principal.listener.event.CreateAdminEvent;
import com.wzkris.principal.listener.event.CreateMemberEvent;
import com.wzkris.principal.listener.event.CreateTenantEvent;
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

    private final NotificationInfoHttpService notificationInfoHttpService;

    @Async
    @EventListener
    public void createTenantEvent(CreateTenantEvent event) {
        NotificationReq req = new NotificationReq(
                Collections.singletonList(event.getReceiverId()), AuthTypeEnum.ADMIN,
                "租户创建成功",
                String.format(
                        "租户：%s创建成功，超级管理员账号：%s，临时登录密码：%s，临时操作密码：%s",
                        event.getTenantName(),
                        event.getUsername(),
                        event.getLoginPwd(),
                        event.getOperPwd()));

        notificationInfoHttpService.send2Users(req);
    }

    @Async
    @EventListener
    public void createAdminEvent(CreateAdminEvent event) {
        NotificationReq req = new NotificationReq(
                Collections.singletonList(event.getReceiverId()), AuthTypeEnum.ADMIN,
                "管理员创建成功",
                String.format("管理员账号：%s创建成功，临时登录密码：%s", event.getUsername(), event.getPassword()));

        notificationInfoHttpService.send2Users(req);
    }

    @Async
    @EventListener
    public void createMemberEvent(CreateMemberEvent event) {
        NotificationReq req = new NotificationReq(
                Collections.singletonList(event.getReceiverId()), AuthTypeEnum.TENANT,
                "租户账号创建成功",
                String.format("租户账号：%s创建成功，临时登录密码：%s", event.getUsername(), event.getPassword()));

        notificationInfoHttpService.send2Users(req);
    }

}
