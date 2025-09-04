package com.wzkris.system.controller;

import com.wzkris.common.core.domain.Result;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateType;
import com.wzkris.common.orm.model.BaseController;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.security.annotation.CheckSystemPerms;
import com.wzkris.common.security.annotation.FieldPerms;
import com.wzkris.common.security.annotation.enums.Rw;
import com.wzkris.system.domain.UserOperateLogDO;
import com.wzkris.system.domain.req.UserOperateLogQueryReq;
import com.wzkris.system.mapper.UserOperateLogMapper;
import com.wzkris.system.service.UserOperateLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 操作日志记录
 *
 * @author wzkris
 */
@Tag(name = "用户操作日志管理")
@RestController
@RequestMapping("/user-operatelog-manage")
@RequiredArgsConstructor
public class UserOperateLogManageController extends BaseController {

    private final UserOperateLogMapper userOperateLogMapper;

    private final UserOperateLogService userOperateLogService;

    @Operation(summary = "分页")
    @GetMapping("/list")
    @CheckSystemPerms("system-mod:operatelog-mng:list")
    @FieldPerms(rw = Rw.READ)
    public Result<Page<UserOperateLogDO>> list(UserOperateLogQueryReq queryReq) {
        startPage();
        List<UserOperateLogDO> list = userOperateLogService.list(queryReq);
        return getDataTable(list);
    }

    @Operation(summary = "删除日志")
    @OperateLog(title = "操作日志", subTitle = "删除日志", operateType = OperateType.DELETE)
    @PostMapping("/remove")
    @CheckSystemPerms("system-mod:operatelog-mng:remove")
    public Result<?> remove(@RequestBody List<Long> operIds) {
        return toRes(userOperateLogMapper.deleteByIds(operIds));
    }

}
