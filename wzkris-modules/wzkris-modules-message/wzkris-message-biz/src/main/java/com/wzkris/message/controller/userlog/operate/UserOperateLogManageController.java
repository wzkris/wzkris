package com.wzkris.message.controller.userlog.operate;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.BaseController;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.security.annotation.CheckUserPerms;
import com.wzkris.message.domain.UserOperateLogDO;
import com.wzkris.message.domain.req.userlog.UserOperateLogQueryReq;
import com.wzkris.message.mapper.UserOperateLogMapper;
import com.wzkris.message.service.UserOperateLogService;
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
@Tag(name = "用户操作日志管理")
@RestController
@RequestMapping("/user-operatelog-manage")
@RequiredArgsConstructor
public class UserOperateLogManageController extends BaseController {

    private final UserOperateLogMapper userOperateLogMapper;

    private final UserOperateLogService userOperateLogService;

    @Operation(summary = "分页")
    @GetMapping("/list")
    @CheckUserPerms("msg-mod:user-operatelog-mng:list")
    public Result<Page<UserOperateLogDO>> list(UserOperateLogQueryReq queryReq) {
        startPage();
        List<UserOperateLogDO> list = userOperateLogService.list(queryReq);
        return getDataTable(list);
    }

}
