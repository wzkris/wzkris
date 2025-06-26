package com.wzkris.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.core.domain.Result;
import com.wzkris.common.core.utils.BeanUtil;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateType;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.security.oauth2.annotation.CheckSystemPerms;
import com.wzkris.common.web.model.BaseController;
import com.wzkris.system.domain.SysMessage;
import com.wzkris.system.domain.dto.SimpleMessageDTO;
import com.wzkris.system.domain.req.SysMessageQueryReq;
import com.wzkris.system.domain.req.SysMessageReq;
import com.wzkris.system.mapper.SysMessageMapper;
import com.wzkris.system.service.SysMessageService;
import com.wzkris.system.service.SysNoticeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
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

    private final SysNoticeService noticeService;

    @Operation(summary = "分页")
    @GetMapping("/list")
    @CheckSystemPerms("sys_message:list")
    public Result<Page<SysMessage>> list(SysMessageQueryReq queryReq) {
        startPage();
        List<SysMessage> list = messageMapper.selectList(this.buildQueryWrapper(queryReq));
        return getDataTable(list);
    }

    private LambdaQueryWrapper<SysMessage> buildQueryWrapper(SysMessageQueryReq queryReq) {
        return new LambdaQueryWrapper<SysMessage>()
                .like(StringUtil.isNotBlank(queryReq.getTitle()), SysMessage::getTitle, queryReq.getTitle())
                .eq(StringUtil.isNotBlank(queryReq.getStatus()), SysMessage::getStatus, queryReq.getStatus())
                .orderByDesc(SysMessage::getMsgId);
    }

    @Operation(summary = "详情")
    @GetMapping("/{msgId}")
    @CheckSystemPerms("sys_message:query")
    public Result<SysMessage> getInfo(@PathVariable Long msgId) {
        return ok(messageMapper.selectById(msgId));
    }

    @Operation(summary = "添加草稿")
    @OperateLog(title = "系统消息", subTitle = "添加草稿", operateType = OperateType.INSERT)
    @PostMapping("/add")
    @CheckSystemPerms("sys_message:add")
    public Result<Void> add(@Valid @RequestBody SysMessageReq req) {
        return toRes(messageMapper.insert(BeanUtil.convert(req, SysMessage.class)));
    }

    @Operation(summary = "修改草稿")
    @OperateLog(title = "系统消息", subTitle = "修改草稿", operateType = OperateType.UPDATE)
    @PostMapping("/edit")
    @CheckSystemPerms("sys_message:edit")
    public Result<Void> edit(@RequestBody SysMessageReq req) {
        noticeService.saveBatch2Users(Collections.singletonList(1L), new SimpleMessageDTO("123", "1", "222"));
        return toRes(messageMapper.updateById(BeanUtil.convert(req, SysMessage.class)));
    }

    @Operation(summary = "删除草稿")
    @OperateLog(title = "系统消息", subTitle = "删除草稿", operateType = OperateType.DELETE)
    @PostMapping("/remove")
    @CheckSystemPerms("sys_message:remove")
    public Result<Void> remove(
            @RequestBody @NotEmpty(message = "{desc.message}{desc.id}{validate.notnull}") List<Long> msgIds) {
        return toRes(messageMapper.deleteByIds(msgIds));
    }

}
