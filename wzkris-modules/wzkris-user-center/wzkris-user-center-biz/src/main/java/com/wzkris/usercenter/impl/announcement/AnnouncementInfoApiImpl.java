package com.wzkris.usercenter.impl.announcement;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.web.model.AbstractApi;
import com.wzkris.common.web.utils.BeanUtil;
import com.wzkris.usercenter.api.announcement.AnnouncementInfoApi;
import com.wzkris.usercenter.api.announcement.request.AnnouncementInfoPageRequest;
import com.wzkris.usercenter.api.announcement.response.AnnouncementInfoResponse;
import com.wzkris.usercenter.domain.AnnouncementInfoDO;
import com.wzkris.usercenter.enums.announcement.AnnouncementStatusEnum;
import com.wzkris.usercenter.service.AnnouncementInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnnouncementInfoApiImpl extends AbstractApi implements AnnouncementInfoApi {

    private final AnnouncementInfoService announcementInfoService;

    @Override
    public Result<Page<AnnouncementInfoResponse>> queryPage(AnnouncementInfoPageRequest request) {
        LambdaQueryWrapper<AnnouncementInfoDO> lqw = Wrappers.lambdaQuery(AnnouncementInfoDO.class)
                .eq(AnnouncementInfoDO::getStatus, AnnouncementStatusEnum.PUBLISH)
                .orderByDesc(AnnouncementInfoDO::getAnnouncementId);
        IPage<AnnouncementInfoDO> page = announcementInfoService.page(request.buildPage(), lqw);
        return ok(Page.of(page, BeanUtil.convert(page.getRecords(), AnnouncementInfoResponse.class)));
    }

}
