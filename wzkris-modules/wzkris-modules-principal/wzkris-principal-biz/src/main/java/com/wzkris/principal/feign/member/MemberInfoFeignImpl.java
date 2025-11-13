package com.wzkris.principal.feign.member;

import com.wzkris.common.web.utils.BeanUtil;
import com.wzkris.principal.domain.MemberInfoDO;
import com.wzkris.principal.domain.MemberSocialInfoDO;
import com.wzkris.principal.domain.TenantInfoDO;
import com.wzkris.principal.domain.TenantPackageInfoDO;
import com.wzkris.principal.feign.admin.req.LoginInfoReq;
import com.wzkris.principal.feign.member.req.QueryMemberPermsReq;
import com.wzkris.principal.feign.member.resp.MemberInfoResp;
import com.wzkris.principal.feign.member.resp.MemberPermissionResp;
import com.wzkris.principal.mapper.MemberInfoMapper;
import com.wzkris.principal.mapper.MemberSocialInfoMapper;
import com.wzkris.principal.mapper.TenantInfoMapper;
import com.wzkris.principal.mapper.TenantPackageInfoMapper;
import com.wzkris.principal.service.PermissionService;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Hidden
@RestController
@RequestMapping("/feign-member")
@RequiredArgsConstructor
public class MemberInfoFeignImpl implements MemberInfoFeign {

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
