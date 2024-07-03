package com.wzkris.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.system.domain.SysNotice;
import com.wzkris.system.mapper.SysNoticeMapper;
import com.wzkris.system.service.SysNoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 公告 服务层实现
 *
 * @author wzkris
 */
@Service
@RequiredArgsConstructor
public class SysNoticeServiceImpl implements SysNoticeService {
    private final SysNoticeMapper sysNoticeMapper;

    /**
     * 查询公告列表
     *
     * @param notice 公告信息
     * @return 公告集合
     */
    @Override
    public List<SysNotice> list(SysNotice notice) {
        LambdaQueryWrapper<SysNotice> lqw = this.buildQueryWrapper(notice);
        return sysNoticeMapper.selectList(lqw);
    }

    private LambdaQueryWrapper<SysNotice> buildQueryWrapper(SysNotice notice) {
        return new LambdaQueryWrapper<SysNotice>()
                .like(StringUtil.isNotBlank(notice.getNoticeTitle()), SysNotice::getNoticeTitle, notice.getNoticeTitle())
                .eq(StringUtil.isNotBlank(notice.getNoticeType()), SysNotice::getNoticeType, notice.getNoticeType())
                .orderByDesc(SysNotice::getNoticeId);
    }

}
