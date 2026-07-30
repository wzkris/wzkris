package com.wzkris.usercenter.controller.announcement;

import com.wzkris.common.core.enums.AuthTypeEnum;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateTypeEnum;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.orm.request.IdListRequest;
import com.wzkris.common.orm.request.IdRequest;
import com.wzkris.common.security.annotation.CheckPerms;
import com.wzkris.usercenter.api.announcement.AnnouncementMngApi;
import com.wzkris.usercenter.api.announcement.request.AnnouncementMngPageRequest;
import com.wzkris.usercenter.api.announcement.request.AnnouncementMngSaveRequest;
import com.wzkris.usercenter.api.announcement.request.AnnouncementMngUpdateRequest;
import com.wzkris.usercenter.api.announcement.response.AnnouncementMngResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "公告管理")
@Validated
@RestController
@RequestMapping("/announcement-manage")
@RequiredArgsConstructor
public class AnnouncementMngController {

    private final AnnouncementMngApi announcementMngApi;

    @Operation(summary = "分页")
    @GetMapping("/query-page")
    @CheckPerms(checkTypes = AuthTypeEnum.ADMIN, value = "system-mod:announcement-mng:page")
    public Result<Page<AnnouncementMngResponse>> queryPage(@ParameterObject AnnouncementMngPageRequest request) {
        return announcementMngApi.queryPage(request);
    }

    @Operation(summary = "详情")
    @GetMapping("/query-info/{id}")
    @CheckPerms(checkTypes = AuthTypeEnum.ADMIN, value = "system-mod:announcement-mng:page")
    public Result<AnnouncementMngResponse> queryInfo(@ParameterObject IdRequest request) {
        return announcementMngApi.queryInfo(request);
    }

    @Operation(summary = "添加草稿")
    @OperateLog(title = "系统消息", subTitle = "添加草稿", type = OperateTypeEnum.INSERT)
    @PostMapping("/save")
    @CheckPerms(checkTypes = AuthTypeEnum.ADMIN, value = "system-mod:announcement-mng:add")
    public Result<Void> save(@Valid @RequestBody AnnouncementMngSaveRequest request) {
        return announcementMngApi.save(request);
    }

    @Operation(summary = "修改草稿")
    @OperateLog(title = "系统消息", subTitle = "修改草稿", type = OperateTypeEnum.UPDATE)
    @PostMapping("/update")
    @CheckPerms(checkTypes = AuthTypeEnum.ADMIN, value = "system-mod:announcement-mng:edit")
    public Result<Void> update(@RequestBody AnnouncementMngUpdateRequest request) {
        return announcementMngApi.update(request);
    }

    @Operation(summary = "删除草稿")
    @OperateLog(title = "系统消息", subTitle = "删除草稿", type = OperateTypeEnum.DELETE)
    @PostMapping("/remove")
    @CheckPerms(checkTypes = AuthTypeEnum.ADMIN, value = "system-mod:announcement-mng:remove")
    public Result<Void> remove(@RequestBody @Valid IdListRequest request) {
        return announcementMngApi.remove(request);
    }

}

