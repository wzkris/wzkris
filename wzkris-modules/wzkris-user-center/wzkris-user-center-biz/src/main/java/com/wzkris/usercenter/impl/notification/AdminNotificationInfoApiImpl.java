package com.wzkris.usercenter.impl.notification;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.web.model.AbstractApi;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.orm.request.IdRequest;
import com.wzkris.common.security.utils.SecurityUtil;
import com.wzkris.usercenter.api.notification.AdminNotificationInfoApi;
import com.wzkris.usercenter.api.notification.request.NotificationInfoPageRequest;
import com.wzkris.usercenter.api.notification.request.UnreadSizeQueryRequest;
import com.wzkris.usercenter.api.notification.response.NotificationInfoResponse;
import com.wzkris.usercenter.mapper.NotificationInfoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminNotificationInfoApiImpl extends AbstractApi implements AdminNotificationInfoApi {

    private final NotificationInfoMapper notificationInfoMapper;

    @Override
    public Result<Page<NotificationInfoResponse>> queryPage(NotificationInfoPageRequest request) {
        IPage<NotificationInfoResponse> page = notificationInfoMapper.pageAdminNotice(
                request.buildPage(), SecurityUtil.getUid(), request.getNotificationType(), request.getRead());
        return ok(Page.of(page));
    }

    @Override
    public Result<Void> markRead(IdRequest request) {
        return toRes(notificationInfoMapper.updateAdminRead(request.getId(), SecurityUtil.getUid()));
    }

    @Override
    public Result<Integer> queryUnreadSize(UnreadSizeQueryRequest request) {
        int count = notificationInfoMapper.selectCountAdminUnread(SecurityUtil.getUid(), request.getNotificationType());
        return ok(count);
    }

}
