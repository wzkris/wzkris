package com.wzkris.system.impl.announcement;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.orm.model.AbstractApi;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.web.utils.BeanUtil;
import com.wzkris.system.api.announcement.AnnouncementMngApi;
import com.wzkris.system.domain.AnnouncementInfoDO;
import com.wzkris.system.mapper.AnnouncementInfoMapper;
import com.wzkris.system.request.common.IdListRequest;
import com.wzkris.system.request.common.IdRequest;
import com.wzkris.system.request.announcement.AnnouncementMngPageRequest;
import com.wzkris.system.request.announcement.AnnouncementMngSaveUpdateRequest;
import com.wzkris.system.response.announcement.AnnouncementMngResponse;
import com.wzkris.system.service.AnnouncementInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnnouncementMngApiImpl extends AbstractApi implements AnnouncementMngApi {

    private final AnnouncementInfoMapper announcementInfoMapper;

    private final AnnouncementInfoService announcementInfoService;

    @Override
    public Result<Page<AnnouncementMngResponse>> queryPage(AnnouncementMngPageRequest request) {
        startPage();
        List<AnnouncementInfoDO> list = announcementInfoMapper.selectList(this.buildQueryWrapper(request));
        return getPageResult(BeanUtil.convert(list, AnnouncementMngResponse.class));
    }

    private LambdaQueryWrapper<AnnouncementInfoDO> buildQueryWrapper(AnnouncementMngPageRequest request) {
        return new LambdaQueryWrapper<AnnouncementInfoDO>()
                .like(StringUtil.isNotBlank(request.getTitle()), AnnouncementInfoDO::getTitle, request.getTitle())
                .eq(request.getStatus() != null, AnnouncementInfoDO::getStatus, request.getStatus())
                .orderByDesc(AnnouncementInfoDO::getAnnouncementId);
    }

    @Override
    public Result<AnnouncementMngResponse> queryInfo(IdRequest request) {
        Long announcementId = request.getId();
        return ok(BeanUtil.convert(announcementInfoMapper.selectById(announcementId), AnnouncementMngResponse.class));
    }

    @Override
    public Result<Void> save(AnnouncementMngSaveUpdateRequest request) {
        AnnouncementInfoDO announcementInfoDO = BeanUtil.convert(request, AnnouncementInfoDO.class);
        announcementInfoDO.setStatus(request.getStatus());
        return toRes(announcementInfoMapper.insert(announcementInfoDO));
    }

    @Override
    public Result<Void> update(AnnouncementMngSaveUpdateRequest request) {
        AnnouncementInfoDO announcementInfoDO = BeanUtil.convert(request, AnnouncementInfoDO.class);
        announcementInfoDO.setStatus(request.getStatus());
        return toRes(announcementInfoMapper.updateById(announcementInfoDO));
    }

    @Override
    public Result<Void> remove(IdListRequest request) {
        return toRes(announcementInfoMapper.deleteByIds(request.getIds()));
    }

}
