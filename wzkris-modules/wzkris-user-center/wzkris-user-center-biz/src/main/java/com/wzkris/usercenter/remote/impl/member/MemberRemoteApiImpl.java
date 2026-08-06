package com.wzkris.usercenter.remote.impl.member;

import cn.binarywang.wx.miniapp.api.WxMaService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.model.UserRole;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.usercenter.domain.MemberInfoDO;
import com.wzkris.usercenter.domain.MemberSocialInfoDO;
import com.wzkris.usercenter.domain.TenantInfoDO;
import com.wzkris.usercenter.domain.TenantPackageInfoDO;
import com.wzkris.usercenter.mapper.MemberSocialInfoMapper;
import com.wzkris.usercenter.remote.api.admin.request.LoginInfoUpdateRequest;
import com.wzkris.usercenter.remote.api.member.MemberRemoteApi;
import com.wzkris.usercenter.remote.api.member.request.MemberPermsQueryRequest;
import com.wzkris.usercenter.remote.api.member.request.MemberQueryRequest;
import com.wzkris.usercenter.remote.api.member.request.TenantIdRequest;
import com.wzkris.usercenter.remote.api.member.response.MemberListResponse;
import com.wzkris.usercenter.remote.api.member.response.MemberQueryResponse;
import com.wzkris.usercenter.request.StringValueRequest;
import com.wzkris.usercenter.service.MemberInfoService;
import com.wzkris.usercenter.service.PermissionService;
import com.wzkris.usercenter.service.TenantInfoService;
import com.wzkris.usercenter.service.TenantPackageInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberRemoteApiImpl implements MemberRemoteApi {

    private final MemberInfoService memberInfoService;

    private final MemberSocialInfoMapper memberSocialInfoMapper;

    private final TenantInfoService tenantInfoService;

    private final TenantPackageInfoService tenantPackageInfoService;

    private final PermissionService permissionService;

    @Autowired
    @Lazy
    private WxMaService wxMaService;

    @Override
    public Result<List<MemberListResponse>> queryList(MemberQueryRequest request) {
        LambdaQueryWrapper<MemberInfoDO> eq = Wrappers.lambdaQuery(MemberInfoDO.class)
                .eq(StringUtil.isNotBlank(request.getPhoneNumber()), MemberInfoDO::getPhoneNumber, request.getPhoneNumber())
                .eq(StringUtil.isNotBlank(request.getUsername()), MemberInfoDO::getUsername, request.getUsername());
        List<MemberInfoDO> list = memberInfoService.list(eq);
        List<MemberListResponse> responseList = new ArrayList<>();
        for (MemberInfoDO member : list) {
            MemberListResponse response = this.toMemberListResponse(member);
            this.retrieveAllStatus(response);
            responseList.add(response);
        }
        return Result.ok(responseList);
    }

    @Override
    public Result<MemberQueryResponse> queryTenantAdministrator(TenantIdRequest request) {
        TenantInfoDO tenant = tenantInfoService.getById(request.getTenantId());
        if (tenant == null || tenant.getAdministrator() == null) {
            return Result.ok(null);
        }
        MemberInfoDO member = memberInfoService.getById(tenant.getAdministrator());
        if (member == null || !request.getTenantId().equals(member.getTenantId())) {
            return Result.ok(null);
        }
        MemberQueryResponse response = this.toMemberQueryResponse(member);
        this.retrieveAllStatus(response);
        return Result.ok(response);
    }

    @Override
    public Result<MemberQueryResponse> queryByWexcxCode(StringValueRequest request) {
        String identifier;
        try {
            identifier = wxMaService
                    .getUserService()
                    .getSessionInfo(request.getValue())
                    .getOpenid();
        } catch (WxErrorException e) {
            log.error("微信小程序换取openid失败", e);
            return Result.apiRequestFail(e.getError().getErrorMsg());
        }
        MemberSocialInfoDO memberSocialInfoDO = memberSocialInfoMapper.selectOneByObj(
                MemberSocialInfoDO::getIdentifier, identifier);
        if (ObjectUtils.isEmpty(memberSocialInfoDO)) {
            return Result.ok(null);
        }
        MemberInfoDO member = memberInfoService.getById(memberSocialInfoDO.getMemberId());
        MemberQueryResponse response = this.toMemberQueryResponse(member);
        this.retrieveAllStatus(response);
        return Result.ok(response);
    }

    @Override
    public Result<List<UserRole>> queryPermission(MemberPermsQueryRequest request) {
        return Result.ok(permissionService.getTenantPermission(
                request.getId(), request.getTenantId()));
    }

    @Override
    public Result<Void> updateLoginInfo(LoginInfoUpdateRequest request) {
        MemberInfoDO memberInfoDO = new MemberInfoDO(request.getId());
        memberInfoDO.setLoginIp(request.getLoginIp());
        memberInfoDO.setLoginDate(request.getLoginDate());
        memberInfoService.updateById(memberInfoDO);
        return Result.ok();
    }

    private void retrieveAllStatus(MemberQueryResponse memberQueryResponse) {
        if (memberQueryResponse == null) {
            return;
        }
        TenantInfoDO tenantInfoDO = tenantInfoService.getById(memberQueryResponse.getTenantId());
        memberQueryResponse.setTenantStatus(tenantInfoDO.getStatus());
        memberQueryResponse.setTenantExpired(tenantInfoDO.getExpireTime());
        TenantPackageInfoDO tenantPackageInfoDO = tenantPackageInfoService.getById(tenantInfoDO.getPackageId());
        memberQueryResponse.setPackageStatus(tenantPackageInfoDO.getStatus());
    }

    private MemberQueryResponse toMemberQueryResponse(MemberInfoDO memberInfoDO) {
        if (memberInfoDO == null) {
            return null;
        }
        MemberQueryResponse response = new MemberQueryResponse();
        response.setId(memberInfoDO.getId());
        response.setTenantId(memberInfoDO.getTenantId());
        response.setUsername(memberInfoDO.getUsername());
        response.setPhoneNumber(memberInfoDO.getPhoneNumber());
        response.setStatus(memberInfoDO.getStatus());
        response.setPassword(memberInfoDO.getPassword());
        return response;
    }

    private MemberListResponse toMemberListResponse(MemberInfoDO memberInfoDO) {
        if (memberInfoDO == null) {
            return null;
        }
        MemberListResponse response = new MemberListResponse();
        response.setId(memberInfoDO.getId());
        response.setTenantId(memberInfoDO.getTenantId());
        response.setUsername(memberInfoDO.getUsername());
        response.setPhoneNumber(memberInfoDO.getPhoneNumber());
        response.setStatus(memberInfoDO.getStatus());
        response.setPassword(memberInfoDO.getPassword());
        return response;
    }

}

