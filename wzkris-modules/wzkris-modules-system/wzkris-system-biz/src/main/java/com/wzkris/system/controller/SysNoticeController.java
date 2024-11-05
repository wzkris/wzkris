package com.wzkris.system.controller;

import cn.hutool.core.util.IdUtil;
import com.wzkris.common.core.domain.Result;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateType;
import com.wzkris.common.orm.model.BaseController;
import com.wzkris.common.orm.page.Page;
import com.wzkris.system.domain.SysNotice;
import com.wzkris.system.mapper.SysNoticeMapper;
import com.wzkris.system.service.SysNoticeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 公告 信息操作处理
 *
 * @author wzkris
 */
@Tag(name = "站内通知管理")
@RestController
@RequestMapping("/notice")
@RequiredArgsConstructor
public class SysNoticeController extends BaseController {
    private final SysNoticeMapper noticeMapper;
    private final SysNoticeService noticeService;

    /**
     * 获取通知公告列表
     */
    @GetMapping("/list")
    @PreAuthorize("@ps.hasPerms('notice:list')")
    public Result<Page<SysNotice>> list(SysNotice notice) {
        startPage();
        List<SysNotice> list = noticeService.list(notice);
        return getDataTable(list);
    }

    /**
     * 根据通知公告编号获取详细信息
     */
    @GetMapping("/{noticeId}")
    @PreAuthorize("@ps.hasPerms('notice:query')")
    public Result<?> getInfo(@PathVariable Long noticeId) {
        return ok(noticeMapper.selectById(noticeId));
    }

    /**
     * 新增通知公告
     */
    @OperateLog(title = "通知公告", operateType = OperateType.INSERT)
    @PostMapping("/add")
    @PreAuthorize("@ps.hasPerms('notice:add')")
    public Result<?> add(@Validated @RequestBody SysNotice notice) {
        notice.setMessageId(IdUtil.fastSimpleUUID());
        return toRes(noticeMapper.insert(notice));
    }

    /**
     * 修改通知公告
     */
    @OperateLog(title = "通知公告", operateType = OperateType.UPDATE)
    @PostMapping("/edit")
    @PreAuthorize("@ps.hasPerms('notice:edit')")
    public Result<?> edit(@Validated @RequestBody SysNotice notice) {
        return toRes(noticeMapper.updateById(notice));
    }

    /**
     * 删除通知公告
     */
    @OperateLog(title = "通知公告", operateType = OperateType.DELETE)
    @PostMapping("/remove")
    @PreAuthorize("@ps.hasPerms('notice:remove')")
    public Result<?> remove(@RequestBody Long[] noticeIds) {
        return toRes(noticeMapper.deleteByIds(Arrays.asList(noticeIds)));
    }
}
