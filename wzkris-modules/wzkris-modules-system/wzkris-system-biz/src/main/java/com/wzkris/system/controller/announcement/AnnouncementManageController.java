package com.wzkris.system.controller.announcement;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateType;
import com.wzkris.common.orm.model.BaseController;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.security.annotation.CheckUserPerms;
import com.wzkris.common.web.utils.BeanUtil;
import com.wzkris.system.domain.AnnouncementInfoDO;
import com.wzkris.system.domain.req.announcement.AnnouncementManageQueryReq;
import com.wzkris.system.domain.req.announcement.AnnouncementManageReq;
import com.wzkris.system.mapper.AnnouncementInfoMapper;
import com.wzkris.system.service.AnnouncementInfoService;
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
public class AnnouncementManageController extends BaseController {

    private final AnnouncementInfoMapper announcementInfoMapper;

    private final AnnouncementInfoService announcementInfoService;

    @Operation(summary = "分页")
    @GetMapping("/list")
    @CheckUserPerms("system-mod:announcement-mng:list")
    public Result<Page<AnnouncementInfoDO>> list(AnnouncementManageQueryReq queryReq) {
        startPage();
        List<AnnouncementInfoDO> list = announcementInfoMapper.selectList(this.buildQueryWrapper(queryReq));
        return getDataTable(list);
    }

    private LambdaQueryWrapper<AnnouncementInfoDO> buildQueryWrapper(AnnouncementManageQueryReq queryReq) {
        return new LambdaQueryWrapper<AnnouncementInfoDO>()
                .like(StringUtil.isNotBlank(queryReq.getTitle()), AnnouncementInfoDO::getTitle, queryReq.getTitle())
                .eq(StringUtil.isNotBlank(queryReq.getStatus()), AnnouncementInfoDO::getStatus, queryReq.getStatus())
                .orderByDesc(AnnouncementInfoDO::getAnnouncementId);
    }

    @Operation(summary = "详情")
    @GetMapping("/{announcementId}")
    @CheckUserPerms("system-mod:announcement-mng:query")
    public Result<AnnouncementInfoDO> query(@PathVariable Long announcementId) {
        return ok(announcementInfoMapper.selectById(announcementId));
    }

    @Operation(summary = "添加草稿")
    @OperateLog(title = "系统消息", subTitle = "添加草稿", operateType = OperateType.INSERT)
    @PostMapping("/add")
    @CheckUserPerms("system-mod:announcement-mng:add")
    public Result<Void> add(@Valid @RequestBody AnnouncementManageReq req) {
        return toRes(announcementInfoMapper.insert(BeanUtil.convert(req, AnnouncementInfoDO.class)));
    }

    @Operation(summary = "修改草稿")
    @OperateLog(title = "系统消息", subTitle = "修改草稿", operateType = OperateType.UPDATE)
    @PostMapping("/edit")
    @CheckUserPerms("system-mod:announcement-mng:edit")
    public Result<Void> edit(@RequestBody AnnouncementManageReq req) {
        return toRes(announcementInfoMapper.updateById(BeanUtil.convert(req, AnnouncementInfoDO.class)));
    }

    @Operation(summary = "删除草稿")
    @OperateLog(title = "系统消息", subTitle = "删除草稿", operateType = OperateType.DELETE)
    @PostMapping("/remove")
    @CheckUserPerms("system-mod:announcement-mng:remove")
    public Result<Void> remove(
            @RequestBody @NotEmpty(message = "{invalidParameter.id.invalid}") List<Long> msgIds) {
        return toRes(announcementInfoMapper.deleteByIds(msgIds));
    }

}
