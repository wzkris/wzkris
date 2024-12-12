package com.wzkris.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.core.domain.Result;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateType;
import com.wzkris.common.orm.page.Page;
import com.wzkris.common.web.model.BaseController;
import com.wzkris.system.domain.SysMessage;
import com.wzkris.system.domain.req.SysMessageQueryReq;
import com.wzkris.system.domain.req.SysMessageSendReq;
import com.wzkris.system.mapper.SysMessageMapper;
import com.wzkris.system.service.SysMessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统消息 操作处理
 *
 * @author wzkris
 */
@Tag(name = "系统消息管理")
@RestController
@RequestMapping("/sys_message")
@RequiredArgsConstructor
public class SysMessageController extends BaseController {
    private final SysMessageMapper messageMapper;
    private final SysMessageService messageService;

    @Operation(summary = "分页")
    @GetMapping("/list")
    @PreAuthorize("@ps.hasPerms('sys_message:list')")
    public Result<Page<SysMessage>> list(SysMessageQueryReq queryReq) {
        startPage();
        List<SysMessage> list = messageMapper.selectList(this.buildQueryWrapper(queryReq));
        return getDataTable(list);
    }

    private LambdaQueryWrapper<SysMessage> buildQueryWrapper(SysMessageQueryReq queryReq) {
        return new LambdaQueryWrapper<SysMessage>()
                .like(StringUtil.isNotBlank(queryReq.getMsgTitle()), SysMessage::getMsgTitle, queryReq.getMsgTitle())
                .eq(StringUtil.isNotBlank(queryReq.getMsgType()), SysMessage::getMsgType, queryReq.getMsgType())
                .eq(StringUtil.isNotBlank(queryReq.getStatus()), SysMessage::getStatus, queryReq.getStatus())
                .orderByDesc(SysMessage::getMsgId);
    }

    @Operation(summary = "详情")
    @GetMapping("/{msgId}")
    @PreAuthorize("@ps.hasPerms('sys_message:query')")
    public Result<?> getInfo(@PathVariable Long msgId) {
        return ok(messageMapper.selectById(msgId));
    }

    @Operation(summary = "添加草稿")
    @OperateLog(title = "系统消息", subTitle = "添加草稿", operateType = OperateType.INSERT)
    @PostMapping("/add")
    @PreAuthorize("@ps.hasPerms('sys_message:add')")
    public Result<?> add(@Valid @RequestBody SysMessage message) {
        return toRes(messageMapper.insert(message));
    }

    @Operation(summary = "发送消息通知")
    @OperateLog(title = "系统消息", subTitle = "发送通知", operateType = OperateType.UPDATE)
    @PostMapping("/send_notify")
    @PreAuthorize("@ps.hasPerms('sys_message:send')")
    public Result<?> send(@Valid @RequestBody SysMessageSendReq sendReq) {
        if (!messageService.checkIsNotify(sendReq.getMsgId())) {
            return fail("只有通知类型的消息才可以发送");
        }
        else if (!messageService.checkIsDraft(sendReq.getMsgId())) {
            return fail("非草稿状态不可以发送");
        }
        return toRes(messageService.sendNotify(sendReq.getUserIds(), sendReq.getMsgId()));
    }

    @Operation(summary = "修改草稿")
    @OperateLog(title = "系统消息", subTitle = "修改草稿", operateType = OperateType.UPDATE)
    @PostMapping("/edit")
    @PreAuthorize("@ps.hasPerms('sys_message:edit')")
    public Result<?> edit(@Validated @RequestBody SysMessage message) {
        if (!messageService.checkIsDraft(message.getMsgId())) {
            return fail("仅草稿可以修改");
        }
        return toRes(messageMapper.updateById(message));
    }

    @Operation(summary = "删除草稿")
    @OperateLog(title = "系统消息", subTitle = "删除草稿", operateType = OperateType.DELETE)
    @PostMapping("/remove")
    @PreAuthorize("@ps.hasPerms('sys_message:remove')")
    public Result<?> remove(@RequestBody List<Long> msgIds) {
        if (!messageService.checkIsDraft(msgIds)) {
            return fail("仅草稿可以修改");
        }
        return toRes(messageMapper.deleteByIds(msgIds));
    }
}
