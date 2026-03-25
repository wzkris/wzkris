package com.wzkris.usercenter.remoteimpl.admin;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.web.utils.BeanUtil;
import com.wzkris.usercenter.domain.AdminInfoDO;
import com.wzkris.usercenter.domain.resp.permission.AdminPermissionResp;
import com.wzkris.usercenter.remoteimpl.admin.req.AdminPermsQueryReq;
import com.wzkris.usercenter.remoteimpl.admin.req.LoginInfoUpdateReq;
import com.wzkris.usercenter.remoteimpl.admin.resp.AdminInfoResp;
import com.wzkris.usercenter.mapper.AdminInfoMapper;
import com.wzkris.usercenter.service.PermissionService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Hidden
@RestController
@RequestMapping("/admin-info-client")
@RequiredArgsConstructor
public class AdminInfoClientImpl {

    private final AdminInfoMapper adminInfoMapper;

    private final PermissionService permissionService;

    @PostMapping("/query-by-username")
    public Result<AdminInfoResp> getByUsername(String username) {
        AdminInfoDO admin = adminInfoMapper.selectByUsername(username);
        return Result.ok(BeanUtil.convert(admin, AdminInfoResp.class));
    }

    @PostMapping("/query-by-phonenumber")
    public Result<AdminInfoResp> getByPhoneNumber(String phoneNumber) {
        AdminInfoDO adminInfoDO = adminInfoMapper.selectByPhoneNumber(phoneNumber);
        return Result.ok(BeanUtil.convert(adminInfoDO, AdminInfoResp.class));
    }

    @PostMapping("/query-permission")
    public Result<AdminPermissionResp> getPermission(AdminPermsQueryReq adminPermsQueryReq) {
        return Result.ok(permissionService.getAdminPermission(
                adminPermsQueryReq.getAdminId(), adminPermsQueryReq.getDeptId()));
    }

    @PostMapping("/update-logininfo")
    public Result<Void> updateLoginInfo(LoginInfoUpdateReq loginInfoUpdateReq) {
        AdminInfoDO adminInfoDO = new AdminInfoDO(loginInfoUpdateReq.getId());
        adminInfoDO.setLoginIp(loginInfoUpdateReq.getLoginIp());
        adminInfoDO.setLoginDate(loginInfoUpdateReq.getLoginDate());

        adminInfoMapper.updateById(adminInfoDO);
        return Result.ok();
    }

}
