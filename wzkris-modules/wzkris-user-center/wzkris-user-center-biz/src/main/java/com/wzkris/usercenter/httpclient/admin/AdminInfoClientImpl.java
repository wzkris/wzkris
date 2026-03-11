package com.wzkris.usercenter.httpclient.admin;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.web.utils.BeanUtil;
import com.wzkris.usercenter.domain.AdminInfoDO;
import com.wzkris.usercenter.httpclient.admin.req.LoginInfoReq;
import com.wzkris.usercenter.httpclient.admin.req.QueryAdminPermsReq;
import com.wzkris.usercenter.httpclient.admin.resp.AdminInfoResp;
import com.wzkris.usercenter.httpclient.admin.resp.AdminPermissionResp;
import com.wzkris.usercenter.mapper.AdminInfoMapper;
import com.wzkris.usercenter.service.PermissionService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@Hidden
@RestController
@RequiredArgsConstructor
public class AdminInfoClientImpl implements AdminInfoClient {

    private final AdminInfoMapper adminInfoMapper;

    private final PermissionService permissionService;

    @Override
    public Result<AdminInfoResp> getByUsername(String username) {
        AdminInfoDO admin = adminInfoMapper.selectByUsername(username);
        return Result.ok(BeanUtil.convert(admin, AdminInfoResp.class));
    }

    @Override
    public Result<AdminInfoResp> getByPhoneNumber(String phoneNumber) {
        AdminInfoDO adminInfoDO = adminInfoMapper.selectByPhoneNumber(phoneNumber);
        return Result.ok(BeanUtil.convert(adminInfoDO, AdminInfoResp.class));
    }

    @Override
    public Result<AdminPermissionResp> getPermission(QueryAdminPermsReq queryAdminPermsReq) {
        return Result.ok(permissionService.getAdminPermission(
                queryAdminPermsReq.getAdminId(), queryAdminPermsReq.getDeptId()));
    }

    @Override
    public Result<Void> updateLoginInfo(LoginInfoReq loginInfoReq) {
        AdminInfoDO adminInfoDO = new AdminInfoDO(loginInfoReq.getId());
        adminInfoDO.setLoginIp(loginInfoReq.getLoginIp());
        adminInfoDO.setLoginDate(loginInfoReq.getLoginDate());

        adminInfoMapper.updateById(adminInfoDO);
        return Result.ok();
    }

}
