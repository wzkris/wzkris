package com.wzkris.usercenter.impl.announcement;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.BeanCopierUtil;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.web.model.AbstractApi;
import com.wzkris.usercenter.api.announcement.AnnouncementPublicApi;
import com.wzkris.usercenter.api.announcement.request.AnnouncementPublicPageRequest;
import com.wzkris.usercenter.api.announcement.response.AnnouncementPublicPageResponse;
import com.wzkris.usercenter.domain.AnnouncementInfoDO;
import com.wzkris.usercenter.enums.announcement.AnnouncementStatusEnum;
import com.wzkris.usercenter.service.AnnouncementInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnnouncementPublicApiImpl extends AbstractApi implements AnnouncementPublicApi {

    private final AnnouncementInfoService announcementInfoService;

    @Override
    public Result<Page<AnnouncementPublicPageResponse>> queryPage(AnnouncementPublicPageRequest request) {
        LambdaQueryWrapper<AnnouncementInfoDO> lqw = Wrappers.lambdaQuery(AnnouncementInfoDO.class)
                .eq(AnnouncementInfoDO::getStatus, AnnouncementStatusEnum.PUBLISH)
                .orderByDesc(AnnouncementInfoDO::getId);
        IPage<AnnouncementInfoDO> page = announcementInfoService.page(request.buildPage(), lqw);
        return ok(Page.of(page, BeanCopierUtil.copyList(page.getRecords(), AnnouncementPublicPageResponse.class)));
    }

}
