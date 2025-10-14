package com.wzkris.message.controller.stafflog;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.BaseController;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.security.annotation.FieldPerms;
import com.wzkris.common.security.annotation.enums.Rw;
import com.wzkris.common.security.utils.LoginStaffUtil;
import com.wzkris.message.domain.UserOperateLogDO;
import com.wzkris.message.domain.req.userlog.UserOperateLogQueryReq;
import com.wzkris.message.service.UserOperateLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "员工操作日志信息")
@RestController
@RequestMapping("/staff-operatelog-info")
@RequiredArgsConstructor
public class StaffOperateLogInfoController extends BaseController {

    private final UserOperateLogService userOperateLogService;

    @Operation(summary = "分页")
    @GetMapping("/list")
    @FieldPerms(rw = Rw.READ)
    public Result<Page<UserOperateLogDO>> list(UserOperateLogQueryReq queryReq) {
        startPage();
        queryReq.setUserId(LoginStaffUtil.getId());
        List<UserOperateLogDO> list = userOperateLogService.list(queryReq);
        return getDataTable(list);
    }

}
