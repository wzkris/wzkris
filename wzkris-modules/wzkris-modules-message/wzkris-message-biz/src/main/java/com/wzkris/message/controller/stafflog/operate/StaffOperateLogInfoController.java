package com.wzkris.message.controller.stafflog.operate;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.BaseController;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.security.utils.StaffUtil;
import com.wzkris.message.domain.req.stafflog.StaffOperateLogQueryReq;
import com.wzkris.message.domain.vo.stafflog.StaffOperateLogInfoVO;
import com.wzkris.message.service.StaffOperateLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "员工个人操作日志信息")
@RestController
@RequestMapping("/staff-operatelog-info")
@RequiredArgsConstructor
public class StaffOperateLogInfoController extends BaseController {

    private final StaffOperateLogService staffOperateLogService;

    @Operation(summary = "分页")
    @GetMapping("/list")
    public Result<Page<StaffOperateLogInfoVO>> list(StaffOperateLogQueryReq queryReq) {
        startPage();
        queryReq.setStaffId(StaffUtil.getId());
        List<StaffOperateLogInfoVO> list = staffOperateLogService.listInfoVO(queryReq);
        return getDataTable(list);
    }

}
