package com.wzkris.system.controller.userlog;

import com.wzkris.common.core.domain.Result;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateType;
import com.wzkris.common.orm.model.BaseController;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.security.annotation.CheckUserPerms;
import com.wzkris.system.domain.UserLoginLogDO;
import com.wzkris.system.domain.req.userlog.UserLoginLogQueryReq;
import com.wzkris.system.mapper.UserLoginLogMapper;
import com.wzkris.system.service.UserLoginLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "用户登录日志管理")
@RestController
@RequestMapping("/user-loginlog-manage")
@RequiredArgsConstructor
public class UserLoginlogManageController extends BaseController {

    private final UserLoginLogService userLoginLogService;

    private final UserLoginLogMapper userLoginLogMapper;

    @Operation(summary = "分页")
    @GetMapping("/list")
    @CheckUserPerms("system-mod:loginlog-mng:list")
    public Result<Page<UserLoginLogDO>> list(UserLoginLogQueryReq queryReq) {
        startPage();
        List<UserLoginLogDO> list = userLoginLogService.list(queryReq);
        return getDataTable(list);
    }

    @Operation(summary = "删除日志")
    @OperateLog(title = "登录日志", subTitle = "删除日志", operateType = OperateType.DELETE)
    @PostMapping("/remove")
    @CheckUserPerms("system-mod:loginlog-mng:remove")
    public Result<?> remove(@RequestBody List<Long> logIds) {
        return toRes(userLoginLogMapper.deleteByIds(logIds));
    }

}
