package com.wzkris.usercenter.remoteimpl.member;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.web.utils.BeanUtil;
import com.wzkris.usercenter.domain.MemberInfoDO;
import com.wzkris.usercenter.domain.MemberSocialInfoDO;
import com.wzkris.usercenter.domain.TenantInfoDO;
import com.wzkris.usercenter.domain.TenantPackageInfoDO;
import com.wzkris.usercenter.domain.resp.permission.MemberPermissionResp;
import com.wzkris.usercenter.remoteimpl.admin.req.LoginInfoUpdateReq;
import com.wzkris.usercenter.remoteimpl.member.req.MemberPermsQueryReq;
import com.wzkris.usercenter.remoteimpl.member.resp.MemberInfoResp;
import com.wzkris.usercenter.mapper.MemberInfoMapper;
import com.wzkris.usercenter.mapper.MemberSocialInfoMapper;
import com.wzkris.usercenter.mapper.TenantInfoMapper;
import com.wzkris.usercenter.mapper.TenantPackageInfoMapper;
import com.wzkris.usercenter.service.PermissionService;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Hidden
@RestController
@RequestMapping("/member-info-client")
@RequiredArgsConstructor
public class MemberInfoClientImpl {

    private final MemberInfoMapper memberInfoMapper;

    private final MemberSocialInfoMapper memberSocialInfoMapper;

    private final TenantInfoMapper tenantInfoMapper;

    private final TenantPackageInfoMapper tenantPackageInfoMapper;

    private final PermissionService permissionService;

    @PostMapping("/query-by-username")
    public Result<MemberInfoResp> getByUsername(String username) {
        MemberInfoDO member = memberInfoMapper.selectByUsername(username);
        MemberInfoResp memberResp = BeanUtil.convert(member, MemberInfoResp.class);
        this.retrieveAllStatus(memberResp);
        return Result.ok(memberResp);
    }

    @PostMapping("/query-by-phonenumber")
    public Result<MemberInfoResp> getByPhoneNumber(String phoneNumber) {
        MemberInfoDO member = memberInfoMapper.selectByPhoneNumber(phoneNumber);
        MemberInfoResp memberResp = BeanUtil.convert(member, MemberInfoResp.class);
        this.retrieveAllStatus(memberResp);
        return Result.ok(memberResp);
    }

    @PostMapping("/query-by-wexcx-identifier")
    public Result<MemberInfoResp> getByWexcxIdentifier(String xcxIdentifier) {
        MemberSocialInfoDO memberSocialInfoDO = memberSocialInfoMapper.selectByIdentifier(xcxIdentifier);
        if (ObjectUtils.isEmpty(memberSocialInfoDO)) {
            return Result.ok(null);
        }

        MemberInfoDO member = memberInfoMapper.selectById(memberSocialInfoDO.getMemberId());
        MemberInfoResp memberResp = BeanUtil.convert(member, MemberInfoResp.class);
        this.retrieveAllStatus(memberResp);
        return Result.ok(memberResp);
    }

    /**
     * 查询状态
     */
    private void retrieveAllStatus(@Nullable MemberInfoResp memberResp) {
        if (memberResp == null) return;
        TenantInfoDO tenant = tenantInfoMapper.selectById(memberResp.getTenantId());
        memberResp.setTenantStatus(tenant.getStatus());
        memberResp.setTenantExpired(tenant.getExpireTime());
        TenantPackageInfoDO tenantPackage = tenantPackageInfoMapper.selectById(tenant.getPackageId());
        memberResp.setPackageStatus(tenantPackage.getStatus());
    }

    @PostMapping("/query-permission")
    public Result<MemberPermissionResp> getPermission(MemberPermsQueryReq memberPermsReq) {
        return Result.ok(permissionService.getTenantPermission(
                memberPermsReq.getMemberId(), memberPermsReq.getTenantId()));
    }

    @PostMapping("/update-logininfo")
    public Result<Void> updateLoginInfo(LoginInfoUpdateReq loginInfoUpdateReq) {
        MemberInfoDO memberInfoDO = new MemberInfoDO(loginInfoUpdateReq.getId());
        memberInfoDO.setLoginIp(loginInfoUpdateReq.getLoginIp());
        memberInfoDO.setLoginDate(loginInfoUpdateReq.getLoginDate());

        memberInfoMapper.updateById(memberInfoDO);
        return Result.ok();
    }

}
