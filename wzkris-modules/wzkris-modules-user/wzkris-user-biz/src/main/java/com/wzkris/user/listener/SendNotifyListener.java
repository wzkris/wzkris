package com.wzkris.user.listener;

import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.system.api.RemoteNotifyApi;
import com.wzkris.system.api.domain.request.SendNotifyReq;
import com.wzkris.user.listener.event.CreateTenantEvent;
import com.wzkris.user.listener.event.CreateUserEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Slf4j
@Component
@RequiredArgsConstructor
public class SendNotifyListener {

    @DubboReference
    private final RemoteNotifyApi remoteNotifyApi;

    @Async
    @EventListener
    public void createTenantEvent(CreateTenantEvent event) {
        SendNotifyReq req = new SendNotifyReq(
                Collections.singletonList(event.getToUserId()), "租户创建成功",
                StringUtil.format("租户：{}创建成功，超级管理员账号：{}，临时登录密码：{}，临时操作密码：{}"
                        , event.getTenantName(), event.getUsername(), event.getLoginPwd(), event.getOperPwd()));

        remoteNotifyApi.sendSystemNotify(req);
    }

    @Async
    @EventListener
    public void createUserEvent(CreateUserEvent event) {
        SendNotifyReq req = new SendNotifyReq(
                Collections.singletonList(event.getToUserId()), "系统用户创建成功",
                StringUtil.format("用户账号：{}创建成功，临时登录密码：{}", event.getUsername(), event.getPassword()));

        remoteNotifyApi.sendSystemNotify(req);
    }
}
