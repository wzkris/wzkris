package com.wzkris.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.core.annotation.group.ValidationGroups;
import com.wzkris.common.core.domain.Result;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateType;
import com.wzkris.common.orm.annotation.CheckFieldPerms;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.security.oauth2.annotation.CheckSystemPerms;
import com.wzkris.common.orm.model.BaseController;
import com.wzkris.system.domain.SysOperLog;
import com.wzkris.system.domain.req.SysOperLogQueryReq;
import com.wzkris.system.mapper.SysOperLogMapper;
import com.wzkris.system.service.SysOperLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 操作日志记录
 *
 * @author wzkris
 */
@Tag(name = "操作日志")
@RestController
@RequestMapping("/operlog")
@RequiredArgsConstructor
public class SysOperlogController extends BaseController {

    private final SysOperLogMapper operLogMapper;

    private final SysOperLogService operLogService;

    @Operation(summary = "分页")
    @GetMapping("/list")
    @CheckSystemPerms("operlog:list")
    @CheckFieldPerms(value = "@ps.hasPerms('operlog:field')", groups = ValidationGroups.Select.class)
    public Result<Page<SysOperLog>> list(SysOperLogQueryReq queryReq) {
        startPage();
        List<SysOperLog> list = operLogMapper.selectList(this.buildQueryWrapper(queryReq));
        return getDataTable(list);
    }

    private LambdaQueryWrapper<SysOperLog> buildQueryWrapper(SysOperLogQueryReq queryReq) {
        return new LambdaQueryWrapper<SysOperLog>()
                .eq(StringUtil.isNotBlank(queryReq.getStatus()), SysOperLog::getStatus, queryReq.getStatus())
                .like(StringUtil.isNotBlank(queryReq.getTitle()), SysOperLog::getTitle, queryReq.getTitle())
                .like(StringUtil.isNotBlank(queryReq.getSubTitle()), SysOperLog::getSubTitle, queryReq.getSubTitle())
                .eq(StringUtil.isNotEmpty(queryReq.getOperType()), SysOperLog::getOperType, queryReq.getOperType())
                .like(StringUtil.isNotBlank(queryReq.getOperName()), SysOperLog::getOperName, queryReq.getOperName())
                .between(
                        queryReq.getParam("beginTime") != null && queryReq.getParam("endTime") != null,
                        SysOperLog::getOperTime,
                        queryReq.getParam("beginTime"),
                        queryReq.getParam("endTime"))
                .orderByDesc(SysOperLog::getOperId);
    }

    @Operation(summary = "删除日志")
    @OperateLog(title = "操作日志", subTitle = "删除日志", operateType = OperateType.DELETE)
    @PostMapping("/remove")
    @CheckSystemPerms("operlog:remove")
    public Result<?> remove(@RequestBody Long[] operIds) {
        return toRes(operLogMapper.deleteByIds(Arrays.asList(operIds)));
    }

}
