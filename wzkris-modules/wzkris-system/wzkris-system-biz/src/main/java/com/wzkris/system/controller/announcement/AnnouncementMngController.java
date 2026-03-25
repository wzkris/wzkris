package com.wzkris.system.controller.announcement;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateTypeEnum;
import com.wzkris.common.orm.model.BaseController;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.security.annotation.CheckAdminPerms;
import com.wzkris.system.api.announcement.AnnouncementMngApi;
import com.wzkris.system.request.announcement.AnnouncementMngQueryRequest;
import com.wzkris.system.request.announcement.AnnouncementMngSaveUpdateRequest;
import com.wzkris.system.response.announcement.AnnouncementMngResponse;
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
@Tag(name = "公告管理")
@Validated
@RestController
@RequestMapping("/announcement-manage")
@RequiredArgsConstructor
public class AnnouncementMngController extends BaseController {

    private final AnnouncementMngApi announcementMngApi;

    @Operation(summary = "分页")
    @GetMapping("/query-page")
    @CheckAdminPerms("system-mod:announcement-mng:page")
    public Result<Page<AnnouncementMngResponse>> queryPage(AnnouncementMngQueryRequest request) {
        return announcementMngApi.queryPage(request);
    }

    @Operation(summary = "详情")
    @GetMapping("/query-info/{announcementId}")
    @CheckAdminPerms("system-mod:announcement-mng:page")
    public Result<AnnouncementMngResponse> queryInfo(@PathVariable Long announcementId) {
        return announcementMngApi.queryInfo(announcementId);
    }

    @Operation(summary = "添加草稿")
    @OperateLog(title = "系统消息", subTitle = "添加草稿", type = OperateTypeEnum.INSERT)
    @PostMapping("/save")
    @CheckAdminPerms("system-mod:announcement-mng:add")
    public Result<Void> save(@Valid @RequestBody AnnouncementMngSaveUpdateRequest request) {
        return announcementMngApi.save(request);
    }

    @Operation(summary = "修改草稿")
    @OperateLog(title = "系统消息", subTitle = "修改草稿", type = OperateTypeEnum.UPDATE)
    @PostMapping("/update")
    @CheckAdminPerms("system-mod:announcement-mng:edit")
    public Result<Void> update(@RequestBody AnnouncementMngSaveUpdateRequest request) {
        return announcementMngApi.update(request);
    }

    @Operation(summary = "删除草稿")
    @OperateLog(title = "系统消息", subTitle = "删除草稿", type = OperateTypeEnum.DELETE)
    @PostMapping("/remove")
    @CheckAdminPerms("system-mod:announcement-mng:remove")
    public Result<Void> remove(@RequestBody @NotEmpty(message = "{invalidParameter.id.invalid}") List<Long> msgIds) {
        return announcementMngApi.remove(msgIds);
    }

}

