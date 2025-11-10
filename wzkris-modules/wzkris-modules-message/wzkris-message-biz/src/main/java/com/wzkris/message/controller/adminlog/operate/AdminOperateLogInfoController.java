package com.wzkris.message.controller.adminlog.operate;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.BaseController;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.security.utils.AdminUtil;
import com.wzkris.message.domain.AdminOperateLogDO;
import com.wzkris.message.domain.req.adminlog.AdminOperateLogQueryReq;
import com.wzkris.message.service.AdminOperateLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "管理员操作日志信息")
@RestController
@RequestMapping("/admin-operatelog-info")
@RequiredArgsConstructor
public class AdminOperateLogInfoController extends BaseController {

    private final AdminOperateLogService adminOperateLogService;

    @Operation(summary = "分页")
    @GetMapping("/list")
    public Result<Page<AdminOperateLogDO>> list(AdminOperateLogQueryReq queryReq) {
        startPage();
        queryReq.setAdminId(AdminUtil.getId());
        List<AdminOperateLogDO> list = adminOperateLogService.list(queryReq);
        return getDataTable(list);
    }

}
