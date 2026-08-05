package com.wzkris.usercenter.remote.impl.member;

import cn.binarywang.wx.miniapp.api.WxMaService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.usercenter.domain.MemberInfoDO;
import com.wzkris.usercenter.domain.MemberSocialInfoDO;
import com.wzkris.usercenter.domain.TenantInfoDO;
import com.wzkris.usercenter.domain.TenantPackageInfoDO;
import com.wzkris.usercenter.mapper.MemberSocialInfoMapper;
import com.wzkris.usercenter.remote.api.admin.request.LoginInfoUpdateRequest;
import com.wzkris.usercenter.remote.api.member.MemberInfoRemoteApi;
import com.wzkris.usercenter.remote.api.member.request.MemberPermsQueryRequest;
import com.wzkris.usercenter.remote.api.member.request.MemberQueryRequest;
import com.wzkris.usercenter.remote.api.member.request.TenantIdRequest;
import com.wzkris.usercenter.remote.api.member.response.MemberInfoResponse;
import com.wzkris.usercenter.remote.api.member.response.MemberPermissionResponse;
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
public class MemberInfoRemoteApiImpl implements MemberInfoRemoteApi {

    private final MemberInfoService memberInfoService;

    private final MemberSocialInfoMapper memberSocialInfoMapper;

    private final TenantInfoService tenantInfoService;

    private final TenantPackageInfoService tenantPackageInfoService;

    private final PermissionService permissionService;

    @Autowired
    @Lazy
    private WxMaService wxMaService;

    @Override
    public Result<List<MemberInfoResponse>> queryList(MemberQueryRequest request) {
        LambdaQueryWrapper<MemberInfoDO> eq = Wrappers.lambdaQuery(MemberInfoDO.class)
                .eq(StringUtil.isNotBlank(request.getPhoneNumber()), MemberInfoDO::getPhoneNumber, request.getPhoneNumber())
                .eq(StringUtil.isNotBlank(request.getUsername()), MemberInfoDO::getUsername, request.getUsername());
        List<MemberInfoDO> list = memberInfoService.list(eq);
        List<MemberInfoResponse> responseList = new ArrayList<>();
        for (MemberInfoDO member : list) {
            MemberInfoResponse response = this.toMemberInfoResponse(member);
            this.retrieveAllStatus(response);
            responseList.add(response);
        }
        return Result.ok(responseList);
    }

    @Override
    public Result<MemberInfoResponse> queryTenantAdministrator(TenantIdRequest request) {
        TenantInfoDO tenant = tenantInfoService.getById(request.getTenantId());
        if (tenant == null || tenant.getAdministrator() == null) {
            return Result.ok(null);
        }
        MemberInfoDO member = memberInfoService.getById(tenant.getAdministrator());
        if (member == null || !request.getTenantId().equals(member.getTenantId())) {
            return Result.ok(null);
        }
        MemberInfoResponse response = this.toMemberInfoResponse(member);
        this.retrieveAllStatus(response);
        return Result.ok(response);
    }

    @Override
    public Result<MemberInfoResponse> queryByWexcxCode(StringValueRequest request) {
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
        MemberInfoResponse response = this.toMemberInfoResponse(member);
        this.retrieveAllStatus(response);
        return Result.ok(response);
    }

    @Override
    public Result<MemberPermissionResponse> queryPermission(MemberPermsQueryRequest request) {
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

    private void retrieveAllStatus(MemberInfoResponse memberInfoResponse) {
        if (memberInfoResponse == null) {
            return;
        }
        TenantInfoDO tenantInfoDO = tenantInfoService.getById(memberInfoResponse.getTenantId());
        memberInfoResponse.setTenantStatus(tenantInfoDO.getStatus());
        memberInfoResponse.setTenantExpired(tenantInfoDO.getExpireTime());
        TenantPackageInfoDO tenantPackageInfoDO = tenantPackageInfoService.getById(tenantInfoDO.getPackageId());
        memberInfoResponse.setPackageStatus(tenantPackageInfoDO.getStatus());
    }

    private MemberInfoResponse toMemberInfoResponse(MemberInfoDO memberInfoDO) {
        if (memberInfoDO == null) {
            return null;
        }
        MemberInfoResponse response = new MemberInfoResponse();
        response.setId(memberInfoDO.getId());
        response.setTenantId(memberInfoDO.getTenantId());
        response.setUsername(memberInfoDO.getUsername());
        response.setPhoneNumber(memberInfoDO.getPhoneNumber());
        response.setStatus(memberInfoDO.getStatus());
        response.setPassword(memberInfoDO.getPassword());
        return response;
    }

}

