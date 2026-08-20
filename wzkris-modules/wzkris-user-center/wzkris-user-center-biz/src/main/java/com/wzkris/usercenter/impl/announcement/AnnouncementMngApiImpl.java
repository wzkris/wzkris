package com.wzkris.usercenter.impl.announcement;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.BeanCopierUtil;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.orm.request.IdListRequest;
import com.wzkris.common.orm.request.IdRequest;
import com.wzkris.common.web.model.AbstractApi;
import com.wzkris.usercenter.api.announcement.AnnouncementMngApi;
import com.wzkris.usercenter.api.announcement.request.AnnouncementMngPageRequest;
import com.wzkris.usercenter.api.announcement.request.AnnouncementMngSaveRequest;
import com.wzkris.usercenter.api.announcement.request.AnnouncementMngUpdateRequest;
import com.wzkris.usercenter.api.announcement.response.AnnouncementMngPageResponse;
import com.wzkris.usercenter.api.announcement.response.AnnouncementMngQueryResponse;
import com.wzkris.usercenter.domain.AnnouncementInfoDO;
import com.wzkris.usercenter.service.AnnouncementInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnnouncementMngApiImpl extends AbstractApi implements AnnouncementMngApi {

    private final AnnouncementInfoService announcementInfoService;

    @Override
    public Result<Page<AnnouncementMngPageResponse>> queryPage(AnnouncementMngPageRequest request) {
        IPage<AnnouncementInfoDO> page = announcementInfoService.page(request.buildPage(), this.buildQueryWrapper(request));
        return ok(Page.of(page, BeanCopierUtil.copyList(page.getRecords(), AnnouncementMngPageResponse.class)));
    }

    private LambdaQueryWrapper<AnnouncementInfoDO> buildQueryWrapper(AnnouncementMngPageRequest request) {
        return new LambdaQueryWrapper<AnnouncementInfoDO>()
                .like(StringUtil.isNotBlank(request.getTitle()), AnnouncementInfoDO::getTitle, request.getTitle())
                .eq(request.getStatus() != null, AnnouncementInfoDO::getStatus, request.getStatus())
                .orderByDesc(AnnouncementInfoDO::getId);
    }

    @Override
    public Result<AnnouncementMngQueryResponse> queryById(IdRequest request) {
        Long announcementId = request.getId();
        return ok(BeanCopierUtil.copy(announcementInfoService.getById(announcementId), AnnouncementMngQueryResponse.class));
    }

    @Override
    public Result<Void> save(AnnouncementMngSaveRequest request) {
        AnnouncementInfoDO announcementInfoDO = BeanCopierUtil.copy(request, AnnouncementInfoDO.class);
        announcementInfoDO.setStatus(request.getStatus());
        return toRes(announcementInfoService.save(announcementInfoDO));
    }

    @Override
    public Result<Void> update(AnnouncementMngUpdateRequest request) {
        AnnouncementInfoDO announcementInfoDO = BeanCopierUtil.copy(request, AnnouncementInfoDO.class);
        announcementInfoDO.setStatus(request.getStatus());
        return toRes(announcementInfoService.updateById(announcementInfoDO));
    }

    @Override
    public Result<Void> remove(IdListRequest request) {
        return toRes(announcementInfoService.removeByIds(request.getIdList()));
    }

}
