package com.wzkris.principal.feign.admin;

import com.wzkris.common.web.utils.BeanUtil;
import com.wzkris.principal.domain.AdminInfoDO;
import com.wzkris.principal.feign.admin.req.LoginInfoReq;
import com.wzkris.principal.feign.admin.req.QueryAdminPermsReq;
import com.wzkris.principal.feign.admin.resp.AdminPermissionResp;
import com.wzkris.principal.feign.admin.resp.adminInfoResp;
import com.wzkris.principal.mapper.AdminInfoMapper;
import com.wzkris.principal.service.PermissionService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Hidden
@RestController
@RequestMapping("/feign-admininfo")
@RequiredArgsConstructor
public class AdminInfoFeignImpl implements AdminInfoFeign {

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
