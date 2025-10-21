package com.wzkris.principal.feign.staff;

import com.wzkris.common.web.utils.BeanUtil;
import com.wzkris.principal.domain.StaffInfoDO;
import com.wzkris.principal.domain.TenantInfoDO;
import com.wzkris.principal.domain.TenantPackageInfoDO;
import com.wzkris.principal.feign.staff.req.QueryStaffPermsReq;
import com.wzkris.principal.feign.staff.resp.StaffInfoResp;
import com.wzkris.principal.feign.staff.resp.StaffPermissionResp;
import com.wzkris.principal.feign.user.req.LoginInfoReq;
import com.wzkris.principal.mapper.StaffInfoMapper;
import com.wzkris.principal.mapper.TenantInfoMapper;
import com.wzkris.principal.mapper.TenantPackageInfoMapper;
import com.wzkris.principal.service.PermissionService;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Hidden
@RestController
@RequestMapping("/feign-staff")
@RequiredArgsConstructor
public class StaffInfoFeignImpl implements StaffInfoFeign {

    private final StaffInfoMapper staffInfoMapper;

    private final TenantInfoMapper tenantInfoMapper;

    private final TenantPackageInfoMapper tenantPackageInfoMapper;

    private final PermissionService permissionService;

    @Override
    public StaffInfoResp getByStaffName(String staffName) {
        StaffInfoDO staff = staffInfoMapper.selectByStaffName(staffName);
        StaffInfoResp staffResp = BeanUtil.convert(staff, StaffInfoResp.class);
        this.retrieveAllStatus(staffResp);
        return staffResp;
    }

    @Override
    public StaffInfoResp getByPhoneNumber(String phoneNumber) {
        StaffInfoDO staff = staffInfoMapper.selectByPhoneNumber(phoneNumber);
        StaffInfoResp staffResp = BeanUtil.convert(staff, StaffInfoResp.class);
        this.retrieveAllStatus(staffResp);
        return staffResp;
    }

    /**
     * 查询状态
     */
    private void retrieveAllStatus(@Nullable StaffInfoResp staffResp) {
        if (staffResp == null) return;
        TenantInfoDO tenant = tenantInfoMapper.selectById(staffResp.getTenantId());
        staffResp.setTenantStatus(tenant.getStatus());
        staffResp.setTenantExpired(tenant.getExpireTime());
        TenantPackageInfoDO tenantPackage = tenantPackageInfoMapper.selectById(tenant.getPackageId());
        staffResp.setPackageStatus(tenantPackage.getStatus());
    }

    @Override
    public StaffPermissionResp getPermission(QueryStaffPermsReq staffPermsReq) {
        return permissionService.getStaffPermission(
                staffPermsReq.getUserId(), staffPermsReq.getTenantId());
    }

    @Override
    public void updateLoginInfo(LoginInfoReq loginInfoReq) {
        StaffInfoDO staffInfoDO = new StaffInfoDO(loginInfoReq.getUserId());
        staffInfoDO.setLoginIp(loginInfoReq.getLoginIp());
        staffInfoDO.setLoginDate(loginInfoReq.getLoginDate());

        staffInfoMapper.updateById(staffInfoDO);
    }

}
