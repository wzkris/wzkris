package com.wzkris.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wzkris.common.core.domain.Result;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.security.utils.LoginUtil;
import com.wzkris.common.web.model.BaseController;
import com.wzkris.system.constant.MessageConstants;
import com.wzkris.system.domain.SysMessage;
import com.wzkris.system.domain.vo.SysAnnouncementVO;
import com.wzkris.system.domain.vo.SysNoticeVO;
import com.wzkris.system.domain.vo.UnreadVO;
import com.wzkris.system.mapper.SysMessageMapper;
import com.wzkris.system.mapper.SysNoticeMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "公告通知读取")
@RestController
@RequestMapping("/message")
@RequiredArgsConstructor
public class SysMessageOwnController extends BaseController {

    private final SysMessageMapper messageMapper;

    private final SysNoticeMapper noticeMapper;

    @Operation(summary = "公告分页")
    @GetMapping("/list")
    public Result<Page<SysAnnouncementVO>> pageResult() {
        LambdaQueryWrapper<SysMessage> lqw = Wrappers.lambdaQuery(SysMessage.class)
                .eq(SysMessage::getStatus, MessageConstants.STATUS_PUBLISH)
                .orderByDesc(SysMessage::getMsgId);
        startPage();
        List<SysAnnouncementVO> list = messageMapper.selectList2VO(lqw, SysAnnouncementVO.class);
        return getDataTable(list);
    }

    @Operation(summary = "通知分页")
    @GetMapping("/notice/list")
    public Result<Page<SysNoticeVO>> listNotice(String readState, String noticeType) {
        startPage();
        List<SysNoticeVO> list = noticeMapper.listNotice(LoginUtil.getUserId(), noticeType, readState);
        return getDataTable(list);
    }

    @Operation(summary = "通知已读")
    @PostMapping("/notice/mark_read")
    public Result<Void> markRead(@RequestBody Long noticeId) {
        return toRes(noticeMapper.markRead(noticeId, LoginUtil.getUserId()));
    }

    @Operation(summary = "未读通知统计")
    @GetMapping("/notice/unread_count")
    public Result<UnreadVO> unreadCount() {
        int count = noticeMapper.countUnread(LoginUtil.getUserId(), null);
        return ok(new UnreadVO(count, 0));
    }
}
