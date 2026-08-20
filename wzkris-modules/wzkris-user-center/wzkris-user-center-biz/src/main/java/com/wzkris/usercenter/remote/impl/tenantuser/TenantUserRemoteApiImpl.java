package com.wzkris.usercenter.remote.impl.tenantuser;

import cn.binarywang.wx.miniapp.api.WxMaService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.model.UserRole;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.usercenter.domain.TenantUserDO;
import com.wzkris.usercenter.domain.TenantUserSocialInfoDO;
import com.wzkris.usercenter.domain.TenantInfoDO;
import com.wzkris.usercenter.domain.TenantPackageInfoDO;
import com.wzkris.usercenter.enums.social.SocialTypeEnum;
import com.wzkris.usercenter.mapper.TenantUserSocialInfoMapper;
import com.wzkris.usercenter.remote.api.admin.request.LoginInfoUpdateRequest;
import com.wzkris.usercenter.remote.api.tenantuser.TenantUserRemoteApi;
import com.wzkris.usercenter.remote.api.tenantuser.request.*;
import com.wzkris.usercenter.remote.api.tenantuser.response.TenantUserQueryResponse;
import com.wzkris.usercenter.service.TenantUserService;
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
public class TenantUserRemoteApiImpl implements TenantUserRemoteApi {

    private final TenantUserService tenantUserService;

    private final TenantUserSocialInfoMapper tenantUserSocialInfoMapper;

    private final TenantInfoService tenantInfoService;

    private final TenantPackageInfoService tenantPackageInfoService;

    private final PermissionService permissionService;

    @Autowired
    @Lazy
    private WxMaService wxMaService;

    @Override
    public Result<List<TenantUserQueryResponse>> queryList(TenantUserQueryRequest request) {
        LambdaQueryWrapper<TenantUserDO> eq = Wrappers.lambdaQuery(TenantUserDO.class)
                .eq(StringUtil.isNotBlank(request.getPhoneNumber()), TenantUserDO::getPhoneNumber, request.getPhoneNumber())
                .eq(StringUtil.isNotBlank(request.getUsername()), TenantUserDO::getUsername, request.getUsername());
        List<TenantUserDO> list = tenantUserService.list(eq);
        List<TenantUserQueryResponse> responseList = new ArrayList<>();
        for (TenantUserDO tenantUser : list) {
            TenantUserQueryResponse response = this.toTenantUserQueryResponse(tenantUser);
            this.retrieveAllStatus(response);
            responseList.add(response);
        }
        return Result.ok(responseList);
    }

    @Override
    public Result<TenantUserQueryResponse> queryTenantAdministrator(TenantIdRequest request) {
        TenantInfoDO tenant = tenantInfoService.getById(request.getTenantId());
        if (tenant == null || tenant.getAdministrator() == null) {
            return Result.ok(null);
        }
        TenantUserDO tenantUser = tenantUserService.getById(tenant.getAdministrator());
        if (tenantUser == null || !request.getTenantId().equals(tenantUser.getTenantId())) {
            return Result.ok(null);
        }
        TenantUserQueryResponse response = this.toTenantUserQueryResponse(tenantUser);
        this.retrieveAllStatus(response);
        return Result.ok(response);
    }

    @Override
    public Result<TenantUserQueryResponse> queryBySocial(SocialQueryRequest request) {
        // 渠道分发：当前仅支持微信小程序，socialType 为空兼容旧调用默认小程序
        SocialTypeEnum socialType = StringUtil.isBlank(request.getSocialType())
                ? SocialTypeEnum.WE_XCX
                : SocialTypeEnum.fromValue(request.getSocialType());
        if (socialType != SocialTypeEnum.WE_XCX) {
            return Result.requestFail("暂不支持该渠道类型查询");
        }
        String identifier;
        try {
            identifier = this.getWeXcxOpenid(request.getWxCode(), request.getAppid());
        } catch (WxErrorException e) {
            log.error("微信小程序换取openid失败", e);
            return Result.apiRequestFail(e.getError().getErrorMsg());
        }
        TenantUserSocialInfoDO tenantUserSocialInfoDO = tenantUserSocialInfoMapper.selectOne(Wrappers
                .<TenantUserSocialInfoDO>lambdaQuery()
                .eq(TenantUserSocialInfoDO::getSocialType, socialType)
                .eq(StringUtil.isNotBlank(request.getAppid()), TenantUserSocialInfoDO::getAppid, request.getAppid())
                .eq(TenantUserSocialInfoDO::getSocialUid, identifier));
        if (ObjectUtils.isEmpty(tenantUserSocialInfoDO)) {
            return Result.ok(null);
        }
        TenantUserDO tenantUser = tenantUserService.getById(tenantUserSocialInfoDO.getTenantUserId());
        TenantUserQueryResponse response = this.toTenantUserQueryResponse(tenantUser);
        this.retrieveAllStatus(response);
        response.setSocialUid(identifier);
        return Result.ok(response);
    }

    @Override
    public Result<List<UserRole>> queryPermission(TenantUserPermissionQueryRequest request) {
        return Result.ok(permissionService.getTenantPermission(
                request.getId(), request.getTenantId()));
    }

    @Override
    public Result<Void> updateLoginInfo(LoginInfoUpdateRequest request) {
        TenantUserDO tenantUserDO = new TenantUserDO(request.getId());
        tenantUserDO.setLoginIp(request.getLoginIp());
        tenantUserDO.setLoginDate(request.getLoginDate());
        tenantUserService.updateById(tenantUserDO);
        return Result.ok();
    }

    /**
     * 更新租户用户社交账号绑定，登录后把当前渠道openid写入social_info，用于支付/渠道识别
     */
    @Override
    public Result<Void> updateSocialInfo(TenantUserSocialUpdateRequest request) {
        if (request.getSocialType() != SocialTypeEnum.WE_XCX) {
            return Result.requestFail("暂不支持该渠道类型绑定");
        }
        String identifier;
        try {
            identifier = this.getWeXcxOpenid(request.getWxCode(), request.getAppid());
        } catch (WxErrorException e) {
            log.error("微信小程序绑定openid查询异常", e);
            return Result.requestFail("微信绑定失败");
        }
        if (StringUtil.isBlank(identifier)) {
            return Result.requestFail("微信绑定失败");
        }

        String appid = StringUtil.isBlank(request.getAppid()) ? null : request.getAppid();
        TenantUserSocialInfoDO exist = tenantUserSocialInfoMapper.selectOne(Wrappers
                .<TenantUserSocialInfoDO>lambdaQuery()
                .eq(TenantUserSocialInfoDO::getSocialType, request.getSocialType())
                .eq(TenantUserSocialInfoDO::getAppid, appid)
                .eq(TenantUserSocialInfoDO::getSocialUid, identifier));
        if (exist != null && !exist.getTenantUserId().equals(request.getTenantUserId())) {
            return Result.requestFail("该微信已绑定其他账号");
        }
        if (exist == null) {
            TenantUserSocialInfoDO socialInfoDO = new TenantUserSocialInfoDO();
            socialInfoDO.setTenantUserId(request.getTenantUserId());
            socialInfoDO.setSocialUid(identifier);
            socialInfoDO.setSocialType(request.getSocialType());
            socialInfoDO.setAppid(appid);
            tenantUserSocialInfoMapper.insert(socialInfoDO);
        }
        return Result.ok();
    }

    /**
     * 按appid切换微信小程序配置换取openid；appid为空则使用默认配置
     */
    private String getWeXcxOpenid(String wxCode, String appid) throws WxErrorException {
        if (StringUtil.isNotBlank(appid)) {
            wxMaService.switchover(appid);
        }
        return wxMaService.getUserService().getSessionInfo(wxCode).getOpenid();
    }

    private void retrieveAllStatus(TenantUserQueryResponse response) {
        if (response == null) {
            return;
        }
        TenantInfoDO tenantInfoDO = tenantInfoService.getById(response.getTenantId());
        response.setTenantStatus(tenantInfoDO.getStatus());
        response.setTenantExpired(tenantInfoDO.getExpireTime());
        TenantPackageInfoDO tenantPackageInfoDO = tenantPackageInfoService.getById(tenantInfoDO.getPackageId());
        response.setPackageStatus(tenantPackageInfoDO.getStatus());
    }

    private TenantUserQueryResponse toTenantUserQueryResponse(TenantUserDO tenantUserDO) {
        if (tenantUserDO == null) {
            return null;
        }
        TenantUserQueryResponse response = new TenantUserQueryResponse();
        response.setId(tenantUserDO.getId());
        response.setTenantId(tenantUserDO.getTenantId());
        response.setUsername(tenantUserDO.getUsername());
        response.setPhoneNumber(tenantUserDO.getPhoneNumber());
        response.setStatus(tenantUserDO.getStatus());
        response.setPassword(tenantUserDO.getPassword());
        return response;
    }

}

