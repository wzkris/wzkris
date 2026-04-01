package com.wzkris.system.impl.announcement;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.AbstractApi;
import com.wzkris.common.orm.model.Page;
import com.wzkris.system.api.announcement.AnnouncementInfoApi;
import com.wzkris.system.domain.AnnouncementInfoDO;
import com.wzkris.system.enums.AnncStatusEnum;
import com.wzkris.system.mapper.AnnouncementInfoMapper;
import com.wzkris.system.response.announcement.AnnouncementInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnnouncementInfoApiImpl extends AbstractApi implements AnnouncementInfoApi {

    private final AnnouncementInfoMapper announcementInfoMapper;

    @Override
    public Result<Page<AnnouncementInfoResponse>> queryPage() {
        LambdaQueryWrapper<AnnouncementInfoDO> lqw = Wrappers.lambdaQuery(AnnouncementInfoDO.class)
                .eq(AnnouncementInfoDO::getStatus, AnncStatusEnum.PUBLISH.getValue())
                .orderByDesc(AnnouncementInfoDO::getAnnouncementId);
        startPage();
        List<AnnouncementInfoResponse> list = announcementInfoMapper.selectList2VO(lqw, AnnouncementInfoResponse.class);
        return getDataTable(list);
    }

}
