package com.wzkris.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wzkris.common.core.domain.Result;
import com.wzkris.common.orm.page.Page;
import com.wzkris.common.security.utils.SysUtil;
import com.wzkris.common.web.model.BaseController;
import com.wzkris.system.constant.MessageConstants;
import com.wzkris.system.domain.SysMessage;
import com.wzkris.system.domain.vo.SysAnnouncementVO;
import com.wzkris.system.domain.vo.SysNotifyVO;
import com.wzkris.system.domain.vo.UnreadVO;
import com.wzkris.system.mapper.SysMessageMapper;
import com.wzkris.system.mapper.SysMessageSendMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "公告通知读取")
@RestController
@RequestMapping("/message")
@RequiredArgsConstructor
public class SysMessageOwnController extends BaseController {

    private final SysMessageMapper messageMapper;
    private final SysMessageSendMapper messageSendMapper;

    @Operation(summary = "公告分页")
    @GetMapping("/announcement/list")
    public Result<Page<SysAnnouncementVO>> announcementList() {
        LambdaQueryWrapper<SysMessage> lqw = Wrappers.lambdaQuery(SysMessage.class)
                .eq(SysMessage::getStatus, MessageConstants.STATUS_SENDED)
                .eq(SysMessage::getMsgType, MessageConstants.TYPE_ANNOUNCEMENT)
                .orderByDesc(SysMessage::getMsgId);
        startPage();
        List<SysAnnouncementVO> list = messageMapper.selectList2VO(lqw, SysAnnouncementVO.class);
        return getDataTable(list);
    }

    @Operation(summary = "通知分页")
    @GetMapping("/notify/list")
    public Result<Page<SysNotifyVO>> notifyList() {
        startPage();
        List<SysNotifyVO> list = messageMapper.listNotify(SysUtil.getUserId());
        return getDataTable(list);
    }

    @Operation(summary = "未读数量统计")
    @GetMapping("/unread_count")
    public Result<UnreadVO> unreadStatistics() {
        int notify = messageSendMapper.countUnreadByUserId(SysUtil.getUserId());
        return ok(new UnreadVO(notify));
    }
}
