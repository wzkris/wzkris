package com.wzkris.usercenter.httpservice.member;

import com.wzkris.common.web.utils.BeanUtil;
import com.wzkris.usercenter.domain.MemberInfoDO;
import com.wzkris.usercenter.domain.MemberSocialInfoDO;
import com.wzkris.usercenter.domain.TenantInfoDO;
import com.wzkris.usercenter.domain.TenantPackageInfoDO;
import com.wzkris.usercenter.httpservice.admin.req.LoginInfoReq;
import com.wzkris.usercenter.httpservice.member.req.QueryMemberPermsReq;
import com.wzkris.usercenter.httpservice.member.resp.MemberInfoResp;
import com.wzkris.usercenter.httpservice.member.resp.MemberPermissionResp;
import com.wzkris.usercenter.mapper.MemberInfoMapper;
import com.wzkris.usercenter.mapper.MemberSocialInfoMapper;
import com.wzkris.usercenter.mapper.TenantInfoMapper;
import com.wzkris.usercenter.mapper.TenantPackageInfoMapper;
import com.wzkris.usercenter.service.PermissionService;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.web.bind.annotation.RestController;

@Hidden
@RestController
@RequiredArgsConstructor
public class MemberInfoHttpServiceImpl implements MemberInfoHttpService {

    private final MemberInfoMapper memberInfoMapper;

    private final MemberSocialInfoMapper memberSocialInfoMapper;

    private final TenantInfoMapper tenantInfoMapper;

    private final TenantPackageInfoMapper tenantPackageInfoMapper;

    private final PermissionService permissionService;

    @Override
    public MemberInfoResp getByUsername(String username) {
        MemberInfoDO member = memberInfoMapper.selectByUsername(username);
        MemberInfoResp memberResp = BeanUtil.convert(member, MemberInfoResp.class);
        this.retrieveAllStatus(memberResp);
        return memberResp;
    }

    @Override
    public MemberInfoResp getByPhoneNumber(String phoneNumber) {
        MemberInfoDO member = memberInfoMapper.selectByPhoneNumber(phoneNumber);
        MemberInfoResp memberResp = BeanUtil.convert(member, MemberInfoResp.class);
        this.retrieveAllStatus(memberResp);
        return memberResp;
    }

    @Override
    public MemberInfoResp getByWexcxIdentifier(String xcxIdentifier) {
        MemberSocialInfoDO memberSocialInfoDO = memberSocialInfoMapper.selectByIdentifier(xcxIdentifier);
        if (ObjectUtils.isEmpty(memberSocialInfoDO)) return null;

        MemberInfoDO member = memberInfoMapper.selectById(memberSocialInfoDO.getMemberId());
        MemberInfoResp memberResp = BeanUtil.convert(member, MemberInfoResp.class);
        this.retrieveAllStatus(memberResp);
        return memberResp;
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

    @Override
    public MemberPermissionResp getPermission(QueryMemberPermsReq memberPermsReq) {
        return permissionService.getTenantPermission(
                memberPermsReq.getMemberId(), memberPermsReq.getTenantId());
    }

    @Override
    public void updateLoginInfo(LoginInfoReq loginInfoReq) {
        MemberInfoDO memberInfoDO = new MemberInfoDO(loginInfoReq.getId());
        memberInfoDO.setLoginIp(loginInfoReq.getLoginIp());
        memberInfoDO.setLoginDate(loginInfoReq.getLoginDate());

        memberInfoMapper.updateById(memberInfoDO);
    }

}
