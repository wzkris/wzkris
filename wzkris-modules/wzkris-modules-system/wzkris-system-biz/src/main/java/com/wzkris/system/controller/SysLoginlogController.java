package com.wzkris.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.core.domain.Result;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateType;
import com.wzkris.common.orm.page.Page;
import com.wzkris.common.web.model.BaseController;
import com.wzkris.system.domain.SysLoginLog;
import com.wzkris.system.domain.req.SysLoginLogQueryReq;
import com.wzkris.system.mapper.SysLoginLogMapper;
import com.wzkris.system.service.SysLoginLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 登录日志， 该模块租户也可访问，需要手动切换租户ID
 * @date : 2023/8/26 16:25
 */
@Tag(name = "登录日志")
@RestController
@RequestMapping("/loginlog")
@RequiredArgsConstructor
public class SysLoginlogController extends BaseController {

    private final SysLoginLogService loginLogService;
    private final SysLoginLogMapper loginLogMapper;

    @Operation(summary = "分页")
    @GetMapping("/list")
    @PreAuthorize("@ps.hasPerms('loginlog:list')")
    public Result<Page<SysLoginLog>> list(SysLoginLogQueryReq queryReq) {
        startPage();
        List<SysLoginLog> list = loginLogMapper.selectList(this.buildQueryWrapper(queryReq));
        return getDataTable(list);
    }

    private LambdaQueryWrapper<SysLoginLog> buildQueryWrapper(SysLoginLogQueryReq queryReq) {
        return new LambdaQueryWrapper<SysLoginLog>()
                .eq(StringUtil.isNotNull(queryReq.getStatus()), SysLoginLog::getStatus, queryReq.getStatus())
                .like(StringUtil.isNotNull(queryReq.getUsername()), SysLoginLog::getUsername, queryReq.getUsername())
                .like(StringUtil.isNotNull(queryReq.getLoginLocation()), SysLoginLog::getLoginLocation, queryReq.getLoginLocation())
                .between(queryReq.getParams().get("beginTime") != null && queryReq.getParams().get("endTime") != null,
                        SysLoginLog::getLoginTime, queryReq.getParams().get("beginTime"), queryReq.getParams().get("endTime"))
                .orderByDesc(SysLoginLog::getLogId);
    }

    @Operation(summary = "删除日志")
    @OperateLog(title = "登录日志", subTitle = "删除日志", operateType = OperateType.DELETE)
    @PostMapping("/remove")
    @PreAuthorize("@ps.hasPerms('loginlog:remove')")
    public Result<?> remove(@RequestBody List<Long> logIds) {
        return toRes(loginLogMapper.deleteByIds(logIds));
    }

    @Operation(summary = "清空日志")
    @OperateLog(title = "登录日志", subTitle = "清空日志", operateType = OperateType.DELETE)
    @PostMapping("/clean")
    @PreAuthorize("@ps.hasPerms('loginlog:remove')")
    public Result<?> clean() {
        loginLogMapper.clearAll();
        return ok();
    }

}
