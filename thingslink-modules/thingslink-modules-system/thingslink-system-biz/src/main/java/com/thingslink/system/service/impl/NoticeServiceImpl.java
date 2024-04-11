package com.thingslink.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.thingslink.common.core.utils.StringUtil;
import com.thingslink.system.domain.Notice;
import com.thingslink.system.mapper.NoticeMapper;
import com.thingslink.system.service.NoticeService;
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
public class NoticeServiceImpl implements NoticeService {
    private final NoticeMapper noticeMapper;

    /**
     * 查询公告列表
     *
     * @param notice 公告信息
     * @return 公告集合
     */
    @Override
    public List<Notice> list(Notice notice) {
        LambdaQueryWrapper<Notice> lqw = this.buildQueryWrapper(notice);
        return noticeMapper.selectList(lqw);
    }

    private LambdaQueryWrapper<Notice> buildQueryWrapper(Notice notice) {
        return new LambdaQueryWrapper<Notice>()
                .like(StringUtil.isNotBlank(notice.getNoticeTitle()), Notice::getNoticeTitle, notice.getNoticeTitle())
                .eq(StringUtil.isNotBlank(notice.getNoticeType()), Notice::getNoticeType, notice.getNoticeType())
                .like(StringUtil.isNotBlank(notice.getCreateBy()), Notice::getCreateBy, notice.getCreateBy())
                .orderByDesc(Notice::getNoticeId);
    }

}
