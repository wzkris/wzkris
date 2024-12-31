package com.wzkris.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wzkris.common.core.domain.Result;
import com.wzkris.common.orm.page.Page;
import com.wzkris.common.security.utils.LoginUserUtil;
import com.wzkris.common.web.model.BaseController;
import com.wzkris.system.constant.MessageConstants;
import com.wzkris.system.domain.SysMessage;
import com.wzkris.system.domain.vo.SysAnnouncementVO;
import com.wzkris.system.domain.vo.SysNotifyVO;
import com.wzkris.system.domain.vo.UnreadVO;
import com.wzkris.system.mapper.SysMessageMapper;
import com.wzkris.system.mapper.SysNotifyMapper;
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
    private final SysNotifyMapper notifyMapper;

    @Operation(summary = "公告分页")
    @GetMapping("/announcement/list")
    public Result<Page<SysAnnouncementVO>> announcementList() {
        LambdaQueryWrapper<SysMessage> lqw = Wrappers.lambdaQuery(SysMessage.class)
                .eq(SysMessage::getStatus, MessageConstants.STATUS_PUBLISH)
                .eq(SysMessage::getMsgType, MessageConstants.TYPE_SYSTEM)
                .orderByDesc(SysMessage::getMsgId);
        startPage();
        List<SysAnnouncementVO> list = messageMapper.selectList2VO(lqw, SysAnnouncementVO.class);
        return getDataTable(list);
    }

    @Operation(summary = "系统通知分页")
    @GetMapping("/system_notify/list")
    public Result<Page<SysNotifyVO>> systemNotify(String readState) {
        startPage();
        List<SysNotifyVO> list = notifyMapper.listNotify(LoginUserUtil.getUserId(), MessageConstants.NOTIFY_TYPE_SYSTEM, readState);
        return getDataTable(list);
    }

    @Operation(summary = "设备通知分页")
    @GetMapping("/device_notify/list")
    public Result<Page<SysNotifyVO>> deviceNotify(String readState) {
        startPage();
        List<SysNotifyVO> list = notifyMapper.listNotify(LoginUserUtil.getUserId(), MessageConstants.NOTIFY_TYPE_DEVICE, readState);
        return getDataTable(list);
    }

    @Operation(summary = "通知读取")
    @PostMapping("/read_notify")
    public Result<Void> readNotify(@RequestBody Long notifyId) {
        return toRes(notifyMapper.readNotify(notifyId, LoginUserUtil.getUserId()));
    }

    @Operation(summary = "未读数量统计")
    @GetMapping("/unread_count")
    public Result<UnreadVO> unreadCount() {
        int system = notifyMapper.countUnread(LoginUserUtil.getUserId(), MessageConstants.NOTIFY_TYPE_SYSTEM);
        int device = notifyMapper.countUnread(LoginUserUtil.getUserId(), MessageConstants.NOTIFY_TYPE_DEVICE);
        return ok(new UnreadVO(system, device));
    }
}
