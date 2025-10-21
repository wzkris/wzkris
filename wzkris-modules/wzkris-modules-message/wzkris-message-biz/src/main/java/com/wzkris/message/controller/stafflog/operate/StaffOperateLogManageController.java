package com.wzkris.message.controller.stafflog.operate;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.BaseController;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.security.annotation.CheckUserPerms;
import com.wzkris.message.domain.req.stafflog.StaffOperateLogQueryReq;
import com.wzkris.message.domain.vo.stafflog.StaffOperateLogInfoVO;
import com.wzkris.message.mapper.StaffOperateLogMapper;
import com.wzkris.message.service.StaffOperateLogService;
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
@Tag(name = "员工操作日志管理")
@RestController
@RequestMapping("/staff-operatelog-manage")
@RequiredArgsConstructor
public class StaffOperateLogManageController extends BaseController {

    private final StaffOperateLogMapper staffOperateLogMapper;

    private final StaffOperateLogService staffOperateLogService;

    @Operation(summary = "分页")
    @GetMapping("/list")
    @CheckUserPerms("msg-mod:staff-operatelog-mng:list")
    public Result<Page<StaffOperateLogInfoVO>> list(StaffOperateLogQueryReq queryReq) {
        startPage();
        List<StaffOperateLogInfoVO> list = staffOperateLogService.listInfoVO(queryReq);
        return getDataTable(list);
    }

}
