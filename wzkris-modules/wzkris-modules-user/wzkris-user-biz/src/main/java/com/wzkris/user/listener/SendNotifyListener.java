package com.wzkris.user.listener;

import com.wzkris.system.rmi.SysNoticeFeign;
import com.wzkris.system.rmi.domain.req.SendNoticeReq;
import com.wzkris.user.listener.event.CreateTenantEvent;
import com.wzkris.user.listener.event.CreateUserEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Slf4j
@Component
@RequiredArgsConstructor
public class SendNotifyListener {

    private final SysNoticeFeign sysNoticeFeign;

    @Async
    @EventListener
    public void createTenantEvent(CreateTenantEvent event) {
        SendNoticeReq req = new SendNoticeReq(
                Collections.singletonList(event.getToUserId()),
                "租户创建成功",
                String.format(
                        "租户：%s创建成功，超级管理员账号：%s，临时登录密码：%s，临时操作密码：%s",
                        event.getTenantName(),
                        event.getUsername(),
                        event.getLoginPwd(),
                        event.getOperPwd()));

        sysNoticeFeign.send2Users(req);
    }

    @Async
    @EventListener
    public void createUserEvent(CreateUserEvent event) {
        SendNoticeReq req = new SendNoticeReq(
                Collections.singletonList(event.getToUserId()),
                "系统用户创建成功",
                String.format("用户账号：%s创建成功，临时登录密码：%s", event.getUsername(), event.getPassword()));

        sysNoticeFeign.send2Users(req);
    }

}
