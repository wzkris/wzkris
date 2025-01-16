package com.wzkris.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.core.domain.Result;
import com.wzkris.common.core.utils.BeanUtil;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateType;
import com.wzkris.common.orm.page.Page;
import com.wzkris.common.security.oauth2.annotation.CheckPerms;
import com.wzkris.common.web.model.BaseController;
import com.wzkris.system.constant.MessageConstants;
import com.wzkris.system.domain.SysMessage;
import com.wzkris.system.domain.req.SysMessageQueryReq;
import com.wzkris.system.domain.req.SysMessageReq;
import com.wzkris.system.mapper.SysMessageMapper;
import com.wzkris.system.service.SysMessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统消息 操作处理
 *
 * @author wzkris
 */
@Tag(name = "系统消息管理")
@Validated
@RestController
@RequestMapping("/sys_message")
@RequiredArgsConstructor
public class SysMessageController extends BaseController {

    private final SysMessageMapper messageMapper;

    private final SysMessageService messageService;

    @Operation(summary = "分页")
    @GetMapping("/list")
    @CheckPerms("sys_message:list")
    public Result<Page<SysMessage>> list(SysMessageQueryReq queryReq) {
        startPage();
        List<SysMessage> list = messageMapper.selectList(this.buildQueryWrapper(queryReq));
        return getDataTable(list);
    }

    private LambdaQueryWrapper<SysMessage> buildQueryWrapper(SysMessageQueryReq queryReq) {
        return new LambdaQueryWrapper<SysMessage>()
                .like(StringUtil.isNotBlank(queryReq.getTitle()), SysMessage::getTitle, queryReq.getTitle())
                .eq(StringUtil.isNotBlank(queryReq.getMsgType()), SysMessage::getMsgType, queryReq.getMsgType())
                .eq(StringUtil.isNotBlank(queryReq.getStatus()), SysMessage::getStatus, queryReq.getStatus())
                .orderByDesc(SysMessage::getMsgId);
    }

    @Operation(summary = "详情")
    @GetMapping("/{msgId}")
    @CheckPerms("sys_message:query")
    public Result<SysMessage> getInfo(@PathVariable Long msgId) {
        return ok(messageMapper.selectById(msgId));
    }

    @Operation(summary = "添加草稿")
    @OperateLog(title = "系统消息", subTitle = "添加草稿", operateType = OperateType.INSERT)
    @PostMapping("/add")
    @CheckPerms("sys_message:add")
    public Result<Void> add(@Valid @RequestBody SysMessageReq req) {
        return toRes(messageMapper.insert(BeanUtil.convert(req, SysMessage.class)));
    }

    @Operation(summary = "修改草稿")
    @OperateLog(title = "系统消息", subTitle = "修改草稿", operateType = OperateType.UPDATE)
    @PostMapping("/edit")
    @CheckPerms("sys_message:edit")
    public Result<Void> edit(@RequestBody SysMessageReq req) {
        if (!messageService.checkIsDraft(req.getMsgId())) {
            return fail("仅草稿可以修改");
        }
        return toRes(messageMapper.updateById(BeanUtil.convert(req, SysMessage.class)));
    }

    @Operation(summary = "发布公告")
    @OperateLog(title = "系统消息", subTitle = "发布公告", operateType = OperateType.UPDATE)
    @PostMapping("/publish")
    @CheckPerms("sys_message:publish")
    public Result<Void> publish(@RequestBody Long msgId) {
        if (!messageService.checkIsDraft(msgId)) {
            return fail("非草稿状态不可以发布");
        }
        SysMessage update = new SysMessage(msgId);
        update.setStatus(MessageConstants.STATUS_PUBLISH);

        return toRes(messageMapper.updateById(update));
    }

    @Operation(summary = "关闭公告")
    @OperateLog(title = "系统消息", subTitle = "关闭公告", operateType = OperateType.UPDATE)
    @PostMapping("/close")
    @CheckPerms("sys_message:send")
    public Result<Void> close(@RequestBody Long msgId) {
        SysMessage update = new SysMessage(msgId);
        update.setStatus(MessageConstants.STATUS_CLOSED);
        return toRes(messageMapper.updateById(update));
    }

    @Operation(summary = "删除草稿")
    @OperateLog(title = "系统消息", subTitle = "删除草稿", operateType = OperateType.DELETE)
    @PostMapping("/remove")
    @CheckPerms("sys_message:remove")
    public Result<Void> remove(@RequestBody @NotEmpty(message = "{desc.message}id{validate.notnull}") List<Long> msgIds) {
        if (!messageService.checkIsClose(msgIds)) {
            return fail("仅关闭消息可以删除");
        }
        return toRes(messageMapper.deleteByIds(msgIds));
    }
}
