package com.thingslink.system.controller;

import cn.hutool.core.util.IdUtil;
import com.thingslink.common.core.domain.Result;
import com.thingslink.common.log.annotation.OperateLog;
import com.thingslink.common.log.enums.OperateType;
import com.thingslink.common.orm.model.BaseController;
import com.thingslink.common.orm.page.Page;
import com.thingslink.system.domain.SysNotice;
import com.thingslink.system.mapper.SysNoticeMapper;
import com.thingslink.system.service.SysNoticeService;
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
    private final SysNoticeMapper sysNoticeMapper;
    private final SysNoticeService sysNoticeService;

    /**
     * 获取通知公告列表
     */
    @GetMapping("/list")
    @PreAuthorize("@ps.hasPerms('notice:list')")
    public Result<Page<SysNotice>> list(SysNotice notice) {
        startPage();
        List<SysNotice> list = sysNoticeService.list(notice);
        return getDataTable(list);
    }

    /**
     * 根据通知公告编号获取详细信息
     */
    @GetMapping("/{noticeId}")
    @PreAuthorize("@ps.hasPerms('notice:query')")
    public Result<?> getInfo(@PathVariable Long noticeId) {
        return success(sysNoticeMapper.selectById(noticeId));
    }

    /**
     * 新增通知公告
     */
    @OperateLog(title = "通知公告", operateType = OperateType.INSERT)
    @PostMapping
    @PreAuthorize("@ps.hasPerms('notice:add')")
    public Result<?> add(@Validated @RequestBody SysNotice notice) {
        notice.setMessageId(IdUtil.fastSimpleUUID());
        return toRes(sysNoticeMapper.insert(notice));
    }

    /**
     * 修改通知公告
     */
    @OperateLog(title = "通知公告", operateType = OperateType.UPDATE)
    @PutMapping
    @PreAuthorize("@ps.hasPerms('notice:edit')")
    public Result<?> edit(@Validated @RequestBody SysNotice notice) {
        return toRes(sysNoticeMapper.updateById(notice));
    }

    /**
     * 删除通知公告
     */
    @OperateLog(title = "通知公告", operateType = OperateType.DELETE)
    @DeleteMapping("{noticeIds}")
    @PreAuthorize("@ps.hasPerms('notice:remove')")
    public Result<?> remove(@PathVariable Long[] noticeIds) {
        return toRes(sysNoticeMapper.deleteBatchIds(Arrays.asList(noticeIds)));
    }
}
