package com.wzkris.system.controller.userlog;

import com.wzkris.common.core.domain.Result;
import com.wzkris.common.orm.model.BaseController;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.security.annotation.CheckUserPerms;
import com.wzkris.common.security.utils.LoginUserUtil;
import com.wzkris.system.domain.UserLoginLogDO;
import com.wzkris.system.domain.req.userlog.UserLoginLogQueryReq;
import com.wzkris.system.service.UserLoginLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "用户登录日志信息")
@RestController
@RequestMapping("/user-loginlog-info")
@RequiredArgsConstructor
public class UserLoginlogInfoController extends BaseController {

    private final UserLoginLogService userLoginLogService;

    @Operation(summary = "分页")
    @GetMapping("/list")
    @CheckUserPerms("system-mod:loginlog-mng:list")
    public Result<Page<UserLoginLogDO>> list(UserLoginLogQueryReq queryReq) {
        startPage();
        queryReq.setUserId(LoginUserUtil.getId());
        List<UserLoginLogDO> list = userLoginLogService.list(queryReq);
        return getDataTable(list);
    }

}
