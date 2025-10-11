package com.wzkris.principal.listener;

import com.wzkris.principal.listener.event.CreateTenantEvent;
import com.wzkris.principal.listener.event.CreateUserEvent;
import com.wzkris.system.feign.notification.NotificationInfoFeign;
import com.wzkris.system.feign.notification.req.NotificationReq;
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

    private final NotificationInfoFeign notificationInfoFeign;

    @Async
    @EventListener
    public void createTenantEvent(CreateTenantEvent event) {
        NotificationReq req = new NotificationReq(
                Collections.singletonList(event.getToUserId()),
                "租户创建成功",
                String.format(
                        "租户：%s创建成功，超级管理员账号：%s，临时登录密码：%s，临时操作密码：%s",
                        event.getTenantName(),
                        event.getUsername(),
                        event.getLoginPwd(),
                        event.getOperPwd()));

        notificationInfoFeign.send2Users(req);
    }

    @Async
    @EventListener
    public void createUserEvent(CreateUserEvent event) {
        NotificationReq req = new NotificationReq(
                Collections.singletonList(event.getToUserId()),
                "用户创建成功",
                String.format("用户账号：%s创建成功，临时登录密码：%s", event.getUsername(), event.getPassword()));

        notificationInfoFeign.send2Users(req);
    }

}
