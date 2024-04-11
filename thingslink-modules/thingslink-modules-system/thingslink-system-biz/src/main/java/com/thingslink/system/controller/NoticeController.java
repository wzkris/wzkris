package com.thingslink.system.controller;

import cn.hutool.core.util.IdUtil;
import com.thingslink.common.core.domain.Result;
import com.thingslink.common.log.annotation.OperateLog;
import com.thingslink.common.log.enums.BusinessType;
import com.thingslink.common.orm.page.Page;
import com.thingslink.common.web.controller.BaseController;
import com.thingslink.system.domain.Notice;
import com.thingslink.system.mapper.NoticeMapper;
import com.thingslink.system.service.NoticeService;
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
public class NoticeController extends BaseController {
    private final NoticeMapper noticeMapper;
    private final NoticeService noticeService;

    /**
     * 获取通知公告列表
     */
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('notice:list')")
    public Result<Page<Notice>> list(Notice notice) {
        startPage();
        List<Notice> list = noticeService.list(notice);
        return getDataTable(list);
    }

    /**
     * 根据通知公告编号获取详细信息
     */
    @GetMapping("/{noticeId}")
    @PreAuthorize("hasAuthority('notice:query')")
    public Result<?> getInfo(@PathVariable Long noticeId) {
        return success(noticeMapper.selectById(noticeId));
    }

    /**
     * 新增通知公告
     */
    @OperateLog(title = "通知公告", businessType = BusinessType.INSERT)
    @PostMapping
    @PreAuthorize("hasAuthority('notice:add')")
    public Result<?> add(@Validated @RequestBody Notice notice) {
        notice.setMessageId(IdUtil.fastSimpleUUID());
        return toRes(noticeMapper.insert(notice));
    }

    /**
     * 修改通知公告
     */
    @OperateLog(title = "通知公告", businessType = BusinessType.UPDATE)
    @PutMapping
    @PreAuthorize("hasAuthority('notice:edit')")
    public Result<?> edit(@Validated @RequestBody Notice notice) {
        return toRes(noticeMapper.updateById(notice));
    }

    /**
     * 删除通知公告
     */
    @OperateLog(title = "通知公告", businessType = BusinessType.DELETE)
    @DeleteMapping("{noticeIds}")
    @PreAuthorize("hasAuthority('notice:remove')")
    public Result<?> remove(@PathVariable Long[] noticeIds) {
        return toRes(noticeMapper.deleteBatchIds(Arrays.asList(noticeIds)));
    }
}
