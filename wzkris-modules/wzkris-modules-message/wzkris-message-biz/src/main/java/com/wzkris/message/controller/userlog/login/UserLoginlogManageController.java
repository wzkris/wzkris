package com.wzkris.message.controller.userlog.login;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.BaseController;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.security.annotation.CheckUserPerms;
import com.wzkris.message.domain.UserLoginLogDO;
import com.wzkris.message.domain.req.userlog.UserLoginLogQueryReq;
import com.wzkris.message.mapper.UserLoginLogMapper;
import com.wzkris.message.service.UserLoginLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @CheckUserPerms("msg-mod:user-loginlog-mng:list")
    public Result<Page<UserLoginLogDO>> list(UserLoginLogQueryReq queryReq) {
        startPage();
        List<UserLoginLogDO> list = userLoginLogService.list(queryReq);
        return getDataTable(list);
    }

}
