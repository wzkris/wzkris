package com.wzkris.usercenter.remote.impl.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.usercenter.domain.AdminInfoDO;
import com.wzkris.usercenter.mapper.AdminInfoMapper;
import com.wzkris.usercenter.remote.api.admin.AdminInfoRemoteApi;
import com.wzkris.usercenter.remote.api.admin.request.AdminPermsQueryRequest;
import com.wzkris.usercenter.remote.api.admin.request.AdminQueryOneRequest;
import com.wzkris.usercenter.remote.api.admin.request.LoginInfoUpdateRequest;
import com.wzkris.usercenter.remote.api.admin.response.AdminInfoResponse;
import com.wzkris.usercenter.remote.api.admin.response.AdminPermissionResponse;
import com.wzkris.usercenter.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminInfoRemoteApiImpl implements AdminInfoRemoteApi {

    private final AdminInfoMapper adminInfoMapper;

    private final PermissionService permissionService;

    @Override
    public Result<AdminInfoResponse> queryOne(AdminQueryOneRequest request) {
        LambdaQueryWrapper<AdminInfoDO> eq = Wrappers.lambdaQuery(AdminInfoDO.class)
                .eq(StringUtil.isNotBlank(request.getPhoneNumber()), AdminInfoDO::getPhoneNumber, request.getPhoneNumber())
                .eq(StringUtil.isNotBlank(request.getUsername()), AdminInfoDO::getUsername, request.getUsername());
        AdminInfoDO adminInfoDO = adminInfoMapper.selectOne(eq);
        return Result.ok(this.toAdminInfoResponse(adminInfoDO));
    }

    @Override
    public Result<AdminPermissionResponse> queryPermission(AdminPermsQueryRequest request) {
        return Result.ok(permissionService.getAdminPermission(
                request.getAdminId(), request.getDeptId()));
    }

    @Override
    public Result<Void> updateLoginInfo(LoginInfoUpdateRequest request) {
        AdminInfoDO adminInfoDO = new AdminInfoDO(request.getId());
        adminInfoDO.setLoginIp(request.getLoginIp());
        adminInfoDO.setLoginDate(request.getLoginDate());
        adminInfoMapper.updateById(adminInfoDO);
        return Result.ok();
    }

    private AdminInfoResponse toAdminInfoResponse(AdminInfoDO adminInfoDO) {
        if (adminInfoDO == null) {
            return null;
        }
        AdminInfoResponse response = new AdminInfoResponse();
        response.setAdminId(adminInfoDO.getAdminId());
        response.setDeptId(adminInfoDO.getDeptId());
        response.setUsername(adminInfoDO.getUsername());
        response.setNickname(adminInfoDO.getNickname());
        response.setEmail(adminInfoDO.getEmail());
        response.setPhoneNumber(adminInfoDO.getPhoneNumber());
        response.setStatus(adminInfoDO.getStatus());
        response.setPassword(adminInfoDO.getPassword());
        return response;
    }

}

