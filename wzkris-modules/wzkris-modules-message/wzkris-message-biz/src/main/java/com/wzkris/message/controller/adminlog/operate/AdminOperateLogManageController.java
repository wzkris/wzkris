package com.wzkris.message.controller.adminlog.operate;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.BaseController;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.security.annotation.CheckUserPerms;
import com.wzkris.message.domain.AdminOperateLogDO;
import com.wzkris.message.domain.req.userlog.AdminOperateLogQueryReq;
import com.wzkris.message.mapper.AdminOperateLogMapper;
import com.wzkris.message.service.AdminOperateLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 操作日志记录
 *
 * @author wzkris
 */
@Tag(name = "管理员操作日志管理")
@RestController
@RequestMapping("/admin-operatelog-manage")
@RequiredArgsConstructor
public class AdminOperateLogManageController extends BaseController {

    private final AdminOperateLogMapper adminOperateLogMapper;

    private final AdminOperateLogService adminOperateLogService;

    @Operation(summary = "分页")
    @GetMapping("/list")
    @CheckUserPerms("msg-mod:admin-operatelog-mng:list")
    public Result<Page<AdminOperateLogDO>> list(AdminOperateLogQueryReq queryReq) {
        startPage();
        List<AdminOperateLogDO> list = adminOperateLogService.list(queryReq);
        return getDataTable(list);
    }

}
