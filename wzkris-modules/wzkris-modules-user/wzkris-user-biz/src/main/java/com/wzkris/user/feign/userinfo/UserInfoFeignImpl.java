package com.wzkris.user.feign.userinfo;

import com.wzkris.common.core.constant.CommonConstants;
import com.wzkris.common.web.utils.BeanUtil;
import com.wzkris.user.domain.TenantInfoDO;
import com.wzkris.user.domain.TenantPackageInfoDO;
import com.wzkris.user.domain.UserInfoDO;
import com.wzkris.user.feign.userinfo.req.LoginInfoReq;
import com.wzkris.user.feign.userinfo.req.QueryPermsReq;
import com.wzkris.user.feign.userinfo.resp.PermissionResp;
import com.wzkris.user.feign.userinfo.resp.UserInfoResp;
import com.wzkris.user.mapper.TenantInfoMapper;
import com.wzkris.user.mapper.TenantPackageInfoMapper;
import com.wzkris.user.mapper.UserInfoMapper;
import com.wzkris.user.service.PermissionService;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Hidden
@RestController
@RequestMapping("/feign-userinfo")
@RequiredArgsConstructor
public class UserInfoFeignImpl implements UserInfoFeign {

    private final UserInfoMapper userInfoMapper;

    private final TenantInfoMapper tenantInfoMapper;

    private final TenantPackageInfoMapper tenantPackageInfoMapper;

    private final PermissionService permissionService;

    @Override
    public UserInfoResp getByUsername(String username) {
        UserInfoDO user = userInfoMapper.selectByUsername(username);
        UserInfoResp userResp = BeanUtil.convert(user, UserInfoResp.class);
        this.retrieveAllStatus(userResp);
        return userResp;
    }

    @Override
    public UserInfoResp getByPhoneNumber(String phoneNumber) {
        UserInfoDO userInfoDO = userInfoMapper.selectByPhoneNumber(phoneNumber);
        UserInfoResp userResp = BeanUtil.convert(userInfoDO, UserInfoResp.class);
        this.retrieveAllStatus(userResp);
        return userResp;
    }

    /**
     * 查询状态
     */
    private void retrieveAllStatus(@Nullable UserInfoResp userResp) {
        if (userResp == null) return;
        if (TenantInfoDO.isSuperTenant(userResp.getTenantId())) {
            userResp.setTenantStatus(CommonConstants.STATUS_ENABLE);
            userResp.setTenantExpired(CommonConstants.NEVER_EXPIRED_TIME);
            userResp.setPackageStatus(CommonConstants.STATUS_ENABLE);
        } else {
            TenantInfoDO tenant = tenantInfoMapper.selectById(userResp.getTenantId());
            userResp.setTenantStatus(tenant.getStatus());
            userResp.setTenantExpired(tenant.getExpireTime());
            TenantPackageInfoDO tenantPackage = tenantPackageInfoMapper.selectById(tenant.getPackageId());
            userResp.setPackageStatus(tenantPackage.getStatus());
        }
    }

    @Override
    public PermissionResp getPermission(QueryPermsReq queryPermsReq) {
        return permissionService.getPermission(
                queryPermsReq.getUserId(), queryPermsReq.getTenantId(), queryPermsReq.getDeptId());
    }

    @Override
    public void updateLoginInfo(LoginInfoReq loginInfoReq) {
        UserInfoDO userInfoDO = new UserInfoDO(loginInfoReq.getUserId());
        userInfoDO.setLoginIp(loginInfoReq.getLoginIp());
        userInfoDO.setLoginDate(loginInfoReq.getLoginDate());

        userInfoMapper.updateById(userInfoDO);
    }

}
