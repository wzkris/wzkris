package com.wzkris.usercenter.httpclient.admin;

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
    public AdminInfoResp getByUsername(String username) {
        AdminInfoDO admin = adminInfoMapper.selectByUsername(username);
        return BeanUtil.convert(admin, AdminInfoResp.class);
    }

    @Override
    public AdminInfoResp getByPhoneNumber(String phoneNumber) {
        AdminInfoDO adminInfoDO = adminInfoMapper.selectByPhoneNumber(phoneNumber);
        return BeanUtil.convert(adminInfoDO, AdminInfoResp.class);
    }

    @Override
    public AdminPermissionResp getPermission(QueryAdminPermsReq queryAdminPermsReq) {
        return permissionService.getAdminPermission(
                queryAdminPermsReq.getAdminId(), queryAdminPermsReq.getDeptId());
    }

    @Override
    public void updateLoginInfo(LoginInfoReq loginInfoReq) {
        AdminInfoDO adminInfoDO = new AdminInfoDO(loginInfoReq.getId());
        adminInfoDO.setLoginIp(loginInfoReq.getLoginIp());
        adminInfoDO.setLoginDate(loginInfoReq.getLoginDate());

        adminInfoMapper.updateById(adminInfoDO);
    }

}
