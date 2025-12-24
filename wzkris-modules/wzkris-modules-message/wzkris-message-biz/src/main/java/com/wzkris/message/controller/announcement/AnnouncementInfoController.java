package com.wzkris.message.controller.announcement;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.BaseController;
import com.wzkris.common.orm.model.Page;
import com.wzkris.message.domain.AnnouncementInfoDO;
import com.wzkris.message.domain.vo.announcement.AnnouncementInfoVO;
import com.wzkris.message.enums.AnncStatusEnum;
import com.wzkris.message.mapper.AnnouncementInfoMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "公告信息")
@RestController
@RequestMapping("/announcement-info")
@RequiredArgsConstructor
public class AnnouncementInfoController extends BaseController {

    private final AnnouncementInfoMapper announcementInfoMapper;

    @Operation(summary = "公告分页")
    @GetMapping("/page")
    public Result<Page<AnnouncementInfoVO>> page() {
        LambdaQueryWrapper<AnnouncementInfoDO> lqw = Wrappers.lambdaQuery(AnnouncementInfoDO.class)
                .eq(AnnouncementInfoDO::getStatus, AnncStatusEnum.PUBLISH.getValue())
                .orderByDesc(AnnouncementInfoDO::getAnnouncementId);
        startPage();
        List<AnnouncementInfoVO> list = announcementInfoMapper.selectList2VO(lqw, AnnouncementInfoVO.class);
        return getDataTable(list);
    }

}
