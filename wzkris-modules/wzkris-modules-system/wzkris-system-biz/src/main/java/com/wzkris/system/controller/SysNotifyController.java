package com.wzkris.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.core.domain.Result;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateType;
import com.wzkris.common.orm.page.Page;
import com.wzkris.common.web.model.BaseController;
import com.wzkris.system.domain.SysNotify;
import com.wzkris.system.domain.req.SysNotifyQueryReq;
import com.wzkris.system.mapper.SysNotifyMapper;
import com.wzkris.system.service.SysNotifyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 公告 信息操作处理
 *
 * @author wzkris
 */
@Tag(name = "站内通知管理")
@RestController
@RequestMapping("/notify")
@RequiredArgsConstructor
public class SysNotifyController extends BaseController {
    private final SysNotifyMapper noticeMapper;
    private final SysNotifyService noticeService;

    @Operation(summary = "分页")
    @GetMapping("/list")
    @PreAuthorize("@ps.hasPerms('notify:list')")
    public Result<Page<SysNotify>> list(SysNotifyQueryReq queryReq) {
        startPage();
        List<SysNotify> list = noticeMapper.selectList(this.buildQueryWrapper(queryReq));
        return getDataTable(list);
    }

    private LambdaQueryWrapper<SysNotify> buildQueryWrapper(SysNotifyQueryReq queryReq) {
        return new LambdaQueryWrapper<SysNotify>()
                .like(StringUtil.isNotBlank(queryReq.getNotifyTitle()), SysNotify::getNotifyTitle, queryReq.getNotifyTitle())
                .eq(StringUtil.isNotBlank(queryReq.getNotifyType()), SysNotify::getNotifyType, queryReq.getNotifyType())
                .eq(StringUtil.isNotBlank(queryReq.getStatus()), SysNotify::getStatus, queryReq.getStatus())
                .orderByDesc(SysNotify::getNotifyId);
    }

    @Operation(summary = "详情")
    @GetMapping("/{notifyId}")
    @PreAuthorize("@ps.hasPerms('notify:query')")
    public Result<?> getInfo(@PathVariable Long notifyId) {
        return ok(noticeMapper.selectById(notifyId));
    }

    @Operation(summary = "新增")
    @OperateLog(title = "通知公告", subTitle = "添加通知", operateType = OperateType.INSERT)
    @PostMapping("/add")
    @PreAuthorize("@ps.hasPerms('notify:add')")
    public Result<?> add(@Validated @RequestBody SysNotify notify) {
        return toRes(noticeMapper.insert(notify));
    }

    @Operation(summary = "修改")
    @OperateLog(title = "通知公告", subTitle = "修改通知", operateType = OperateType.UPDATE)
    @PostMapping("/edit")
    @PreAuthorize("@ps.hasPerms('notify:edit')")
    public Result<?> edit(@Validated @RequestBody SysNotify notify) {
        return toRes(noticeMapper.updateById(notify));
    }

    @Operation(summary = "删除")
    @OperateLog(title = "通知公告", subTitle = "删除通知", operateType = OperateType.DELETE)
    @PostMapping("/remove")
    @PreAuthorize("@ps.hasPerms('notify:remove')")
    public Result<?> remove(@RequestBody List<Long> notifyIds) {
        return toRes(noticeMapper.deleteByIds(notifyIds));
    }
}
