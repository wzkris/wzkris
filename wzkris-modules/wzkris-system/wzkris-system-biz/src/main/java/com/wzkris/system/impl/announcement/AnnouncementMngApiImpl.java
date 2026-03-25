package com.wzkris.system.impl.announcement;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.orm.model.BaseController;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.web.utils.BeanUtil;
import com.wzkris.system.api.announcement.AnnouncementMngApi;
import com.wzkris.system.domain.AnnouncementInfoDO;
import com.wzkris.system.mapper.AnnouncementInfoMapper;
import com.wzkris.system.request.announcement.AnnouncementMngQueryRequest;
import com.wzkris.system.request.announcement.AnnouncementMngSaveUpdateRequest;
import com.wzkris.system.response.announcement.AnnouncementMngResponse;
import com.wzkris.system.service.AnnouncementInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnnouncementMngApiImpl extends BaseController implements AnnouncementMngApi {

    private final AnnouncementInfoMapper announcementInfoMapper;

    private final AnnouncementInfoService announcementInfoService;

    @Override
    public Result<Page<AnnouncementMngResponse>> queryPage(AnnouncementMngQueryRequest request) {
        startPage();
        List<AnnouncementInfoDO> list = announcementInfoMapper.selectList(this.buildQueryWrapper(request));
        return getDataTable(BeanUtil.convert(list, AnnouncementMngResponse.class));
    }

    private LambdaQueryWrapper<AnnouncementInfoDO> buildQueryWrapper(AnnouncementMngQueryRequest request) {
        return new LambdaQueryWrapper<AnnouncementInfoDO>()
                .like(StringUtil.isNotBlank(request.getTitle()), AnnouncementInfoDO::getTitle, request.getTitle())
                .eq(StringUtil.isNotBlank(request.getStatus()), AnnouncementInfoDO::getStatus, request.getStatus())
                .orderByDesc(AnnouncementInfoDO::getAnnouncementId);
    }

    @Override
    public Result<AnnouncementMngResponse> queryInfo(Long announcementId) {
        return ok(BeanUtil.convert(announcementInfoMapper.selectById(announcementId), AnnouncementMngResponse.class));
    }

    @Override
    public Result<Void> save(AnnouncementMngSaveUpdateRequest request) {
        return toRes(announcementInfoMapper.insert(BeanUtil.convert(request, AnnouncementInfoDO.class)));
    }

    @Override
    public Result<Void> update(AnnouncementMngSaveUpdateRequest request) {
        return toRes(announcementInfoMapper.updateById(BeanUtil.convert(request, AnnouncementInfoDO.class)));
    }

    @Override
    public Result<Void> remove(List<Long> msgIds) {
        return toRes(announcementInfoMapper.deleteByIds(msgIds));
    }

}
