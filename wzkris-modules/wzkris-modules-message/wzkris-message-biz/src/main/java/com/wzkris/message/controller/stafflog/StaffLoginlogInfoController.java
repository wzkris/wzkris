package com.wzkris.message.controller.stafflog;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.BaseController;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.security.utils.LoginStaffUtil;
import com.wzkris.message.domain.StaffLoginLogDO;
import com.wzkris.message.domain.req.stafflog.StaffLoginLogQueryReq;
import com.wzkris.message.service.StaffLoginLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "员工登录日志信息")
@RestController
@RequestMapping("/staff-loginlog-info")
@RequiredArgsConstructor
public class StaffLoginlogInfoController extends BaseController {

    private final StaffLoginLogService staffLoginLogService;

    @Operation(summary = "分页")
    @GetMapping("/list")
    public Result<Page<StaffLoginLogDO>> list(StaffLoginLogQueryReq queryReq) {
        startPage();
        queryReq.setStaffId(LoginStaffUtil.getId());
        List<StaffLoginLogDO> list = staffLoginLogService.list(queryReq);
        return getDataTable(list);
    }

}
