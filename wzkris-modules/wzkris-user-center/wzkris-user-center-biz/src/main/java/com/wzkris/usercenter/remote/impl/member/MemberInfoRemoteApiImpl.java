package com.wzkris.usercenter.remote.impl.member;

import cn.binarywang.wx.miniapp.api.WxMaService;
import com.wzkris.common.core.model.Result;
import com.wzkris.usercenter.domain.MemberInfoDO;
import com.wzkris.usercenter.domain.MemberSocialInfoDO;
import com.wzkris.usercenter.domain.TenantInfoDO;
import com.wzkris.usercenter.domain.TenantPackageInfoDO;
import com.wzkris.usercenter.mapper.MemberInfoMapper;
import com.wzkris.usercenter.mapper.MemberSocialInfoMapper;
import com.wzkris.usercenter.mapper.TenantInfoMapper;
import com.wzkris.usercenter.mapper.TenantPackageInfoMapper;
import com.wzkris.usercenter.remote.api.admin.request.LoginInfoUpdateRequest;
import com.wzkris.usercenter.remote.api.member.MemberInfoRemoteApi;
import com.wzkris.usercenter.remote.api.member.request.MemberPermsQueryRequest;
import com.wzkris.usercenter.remote.api.member.response.MemberInfoResponse;
import com.wzkris.usercenter.response.permission.MemberPermissionResponse;
import com.wzkris.usercenter.service.PermissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberInfoRemoteApiImpl implements MemberInfoRemoteApi {

    private final MemberInfoMapper memberInfoMapper;

    private final MemberSocialInfoMapper memberSocialInfoMapper;

    private final TenantInfoMapper tenantInfoMapper;

    private final TenantPackageInfoMapper tenantPackageInfoMapper;

    private final PermissionService permissionService;

    @Autowired
    @Lazy
    private WxMaService wxMaService;

    @Override
    public Result<MemberInfoResponse> queryByUsername(String username) {
        MemberInfoDO member = memberInfoMapper.selectByUsername(username);
        MemberInfoResponse response = this.toMemberInfoResponse(member);
        this.retrieveAllStatus(response);
        return Result.ok(response);
    }

    @Override
    public Result<MemberInfoResponse> queryByPhoneNumber(String phoneNumber) {
        MemberInfoDO member = memberInfoMapper.selectByPhoneNumber(phoneNumber);
        MemberInfoResponse response = this.toMemberInfoResponse(member);
        this.retrieveAllStatus(response);
        return Result.ok(response);
    }

    @Override
    public Result<MemberInfoResponse> queryByWexcxCode(String xcxcode) {
        String identifier;
        try {
            identifier = wxMaService
                    .getUserService()
                    .getSessionInfo(xcxcode)
                    .getOpenid();
        } catch (WxErrorException e) {
            log.error("微信小程序换取openid失败", e);
            return Result.apiRequestFail(e.getError().getErrorMsg());
        }
        MemberSocialInfoDO memberSocialInfoDO = memberSocialInfoMapper.selectByIdentifier(identifier);
        if (ObjectUtils.isEmpty(memberSocialInfoDO)) {
            return Result.ok(null);
        }
        MemberInfoDO member = memberInfoMapper.selectById(memberSocialInfoDO.getMemberId());
        MemberInfoResponse response = this.toMemberInfoResponse(member);
        this.retrieveAllStatus(response);
        return Result.ok(response);
    }

    @Override
    public Result<MemberPermissionResponse> queryPermission(MemberPermsQueryRequest request) {
        return Result.ok(permissionService.getTenantPermission(
                request.getMemberId(), request.getTenantId()));
    }

    @Override
    public Result<Void> updateLoginInfo(LoginInfoUpdateRequest request) {
        MemberInfoDO memberInfoDO = new MemberInfoDO(request.getId());
        memberInfoDO.setLoginIp(request.getLoginIp());
        memberInfoDO.setLoginDate(request.getLoginDate());
        memberInfoMapper.updateById(memberInfoDO);
        return Result.ok();
    }

    private void retrieveAllStatus(MemberInfoResponse memberInfoResponse) {
        if (memberInfoResponse == null) {
            return;
        }
        TenantInfoDO tenantInfoDO = tenantInfoMapper.selectById(memberInfoResponse.getTenantId());
        memberInfoResponse.setTenantStatus(tenantInfoDO.getStatus());
        memberInfoResponse.setTenantExpired(tenantInfoDO.getExpireTime());
        TenantPackageInfoDO tenantPackageInfoDO = tenantPackageInfoMapper.selectById(tenantInfoDO.getPackageId());
        memberInfoResponse.setPackageStatus(tenantPackageInfoDO.getStatus());
    }

    private MemberInfoResponse toMemberInfoResponse(MemberInfoDO memberInfoDO) {
        if (memberInfoDO == null) {
            return null;
        }
        MemberInfoResponse response = new MemberInfoResponse();
        response.setMemberId(memberInfoDO.getMemberId());
        response.setTenantId(memberInfoDO.getTenantId());
        response.setUsername(memberInfoDO.getUsername());
        response.setPhoneNumber(memberInfoDO.getPhoneNumber());
        response.setStatus(memberInfoDO.getStatus());
        response.setPassword(memberInfoDO.getPassword());
        return response;
    }

}

