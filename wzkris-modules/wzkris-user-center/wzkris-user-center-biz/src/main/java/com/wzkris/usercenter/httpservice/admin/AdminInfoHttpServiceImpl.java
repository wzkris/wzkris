package com.wzkris.usercenter.httpservice.admin;

import com.wzkris.common.web.utils.BeanUtil;
import com.wzkris.usercenter.domain.AdminInfoDO;
import com.wzkris.usercenter.httpservice.admin.req.LoginInfoReq;
import com.wzkris.usercenter.httpservice.admin.req.QueryAdminPermsReq;
import com.wzkris.usercenter.httpservice.admin.resp.AdminPermissionResp;
import com.wzkris.usercenter.httpservice.admin.resp.adminInfoResp;
import com.wzkris.usercenter.mapper.AdminInfoMapper;
import com.wzkris.usercenter.service.PermissionService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@Hidden
@RestController
@RequiredArgsConstructor
public class AdminInfoHttpServiceImpl implements AdminInfoHttpService {

    private final AdminInfoMapper adminInfoMapper;

    private final PermissionService permissionService;

    @Override
    public adminInfoResp getByUsername(String username) {
        AdminInfoDO admin = adminInfoMapper.selectByUsername(username);
        return BeanUtil.convert(admin, adminInfoResp.class);
    }

    @Override
    public adminInfoResp getByPhoneNumber(String phoneNumber) {
        AdminInfoDO adminInfoDO = adminInfoMapper.selectByPhoneNumber(phoneNumber);
        return BeanUtil.convert(adminInfoDO, adminInfoResp.class);
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
