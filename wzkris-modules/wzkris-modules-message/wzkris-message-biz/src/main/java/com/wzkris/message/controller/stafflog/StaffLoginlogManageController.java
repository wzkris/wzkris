package com.wzkris.message.controller.stafflog;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateType;
import com.wzkris.common.orm.model.BaseController;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.security.annotation.CheckUserPerms;
import com.wzkris.message.domain.StaffLoginLogDO;
import com.wzkris.message.domain.req.stafflog.StaffLoginLogQueryReq;
import com.wzkris.message.mapper.StaffLoginLogMapper;
import com.wzkris.message.service.StaffLoginLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "员工登录日志管理")
@RestController
@RequestMapping("/staff-loginlog-manage")
@RequiredArgsConstructor
public class StaffLoginlogManageController extends BaseController {

    private final StaffLoginLogService staffLoginLogService;

    private final StaffLoginLogMapper staffLoginLogMapper;

    @Operation(summary = "分页")
    @GetMapping("/list")
    @CheckUserPerms("msg-mod:staff-loginlog-mng:list")
    public Result<Page<StaffLoginLogDO>> list(StaffLoginLogQueryReq queryReq) {
        startPage();
        List<StaffLoginLogDO> list = staffLoginLogService.list(queryReq);
        return getDataTable(list);
    }

    @Operation(summary = "删除日志")
    @OperateLog(title = "登录日志", subTitle = "删除日志", operateType = OperateType.DELETE)
    @PostMapping("/remove")
    @CheckUserPerms("msg-mod:staff-loginlog-mng:remove")
    public Result<?> remove(@RequestBody List<Long> logIds) {
        return toRes(staffLoginLogMapper.deleteByIds(logIds));
    }

}
