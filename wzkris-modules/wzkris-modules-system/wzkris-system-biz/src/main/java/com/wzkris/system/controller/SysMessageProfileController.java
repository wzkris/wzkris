package com.wzkris.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wzkris.common.core.domain.Result;
import com.wzkris.common.orm.model.BaseController;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.security.utils.SystemUserUtil;
import com.wzkris.system.constant.MessageConstants;
import com.wzkris.system.domain.SysMessage;
import com.wzkris.system.domain.vo.SysAnnouncementVO;
import com.wzkris.system.domain.vo.SysNoticeVO;
import com.wzkris.system.mapper.SysMessageMapper;
import com.wzkris.system.mapper.SysNoticeMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "通知信息")
@RestController
@RequestMapping("/message_profile")
@RequiredArgsConstructor
public class SysMessageProfileController extends BaseController {

    private final SysMessageMapper messageMapper;

    private final SysNoticeMapper noticeMapper;

    @Operation(summary = "公告分页")
    @GetMapping("/announcement")
    public Result<Page<SysAnnouncementVO>> announcement() {
        LambdaQueryWrapper<SysMessage> lqw = Wrappers.lambdaQuery(SysMessage.class)
                .eq(SysMessage::getStatus, MessageConstants.STATUS_PUBLISH)
                .orderByDesc(SysMessage::getMsgId);
        startPage();
        List<SysAnnouncementVO> list = messageMapper.selectList2VO(lqw, SysAnnouncementVO.class);
        return getDataTable(list);
    }

    @Operation(summary = "通知分页")
    @GetMapping("/notice")
    public Result<Page<SysNoticeVO>> notice(String readState, String noticeType) {
        startPage();
        List<SysNoticeVO> list = noticeMapper.listNotice(SystemUserUtil.getUserId(), noticeType, readState);
        return getDataTable(list);
    }

    @Operation(summary = "标记已读")
    @PostMapping("/notice/mark_read")
    public Result<Void> markRead(@RequestBody Long noticeId) {
        return toRes(noticeMapper.markRead(noticeId, SystemUserUtil.getUserId()));
    }

    @Operation(summary = "未读数量")
    @GetMapping("/notice/unread_size")
    public Result<Integer> unreadSize(String noticeType) {
        int count = noticeMapper.selectUnreadSize(SystemUserUtil.getUserId(), noticeType);
        return ok(count);
    }

}
