package com.wzkris.usercenter.impl.tenantuser;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.model.UserRole;
import com.wzkris.common.core.support.LoginUser;
import com.wzkris.common.core.utils.ResultUtil;
import com.wzkris.common.security.utils.SecurityUtil;
import com.wzkris.common.web.model.AbstractApi;
import com.wzkris.usercenter.api.tenantuser.TenantUserInfoApi;
import com.wzkris.usercenter.api.tenantuser.request.TenantUserInfoBasicUpdateRequest;
import com.wzkris.usercenter.api.tenantuser.response.TenantUserInfoQueryResponse;
import com.wzkris.usercenter.domain.TenantUserDO;
import com.wzkris.usercenter.remote.interfaces.captcha.ICaptchaRemote;
import com.wzkris.usercenter.remote.interfaces.captcha.request.CaptchaCheckRequest;
import com.wzkris.usercenter.request.PasswordUpdateRequest;
import com.wzkris.usercenter.request.PhoneNumberUpdateRequest;
import com.wzkris.usercenter.service.TenantUserService;
import com.wzkris.usercenter.service.TenantRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TenantUserInfoApiImpl extends AbstractApi implements TenantUserInfoApi {

    private final TenantUserService tenantUserService;

    private final TenantRoleService tenantRoleService;

    private final ICaptchaRemote captchaRemote;

    private final PasswordEncoder passwordEncoder;

    @Override
    public Result<TenantUserInfoQueryResponse> query() {
        LoginUser loginUser = SecurityUtil.getLoginUser();
        TenantUserDO tenantUser = tenantUserService.getById(loginUser.getUid());
        TenantUserInfoQueryResponse tenantUserInfoVO = new TenantUserInfoQueryResponse();
        tenantUserInfoVO.setAdmin(SecurityUtil.isSuperUser());
        tenantUserInfoVO.setUsername(tenantUser.getUsername());
        tenantUserInfoVO.setAuthorities(SecurityUtil.getPermission());
        tenantUserInfoVO.setAvatar(tenantUser.getAvatar());
        tenantUserInfoVO.setPhoneNumber(tenantUser.getPhoneNumber());
        tenantUserInfoVO.setGender(tenantUser.getGender());
        tenantUserInfoVO.setLoginDate(tenantUser.getLoginDate());
        tenantUserInfoVO.setRoleGroup(SecurityUtil.getRoleContext().getRoles().stream()
                .map(UserRole::getName).collect(Collectors.joining(",")));
        return ok(tenantUserInfoVO);
    }

    @Override
    public Result<Void> updateBasicInfo(TenantUserInfoBasicUpdateRequest request) {
        TenantUserDO tenantUserDO = new TenantUserDO(SecurityUtil.getUid());
        tenantUserDO.setGender(request.getGender());
        tenantUserDO.setAvatar(request.getAvatar());
        return toRes(tenantUserService.updateById(tenantUserDO));
    }

    @Override
    public Result<Void> updatePhoneNumber(PhoneNumberUpdateRequest request) {
        Long uid = SecurityUtil.getUid();
        if (tenantUserService.existByPhoneNumber(uid, request.getPhoneNumber())) {
            return requestFail("该手机号已被使用");
        }
        CaptchaCheckRequest captchaCheckRequest = new CaptchaCheckRequest();
        TenantUserDO tenantUserDO = tenantUserService.getById(uid);
        captchaCheckRequest.setKey(tenantUserDO.getPhoneNumber());
        captchaCheckRequest.setValue(request.getSmsCode());
        Result<Boolean> captchaResult = captchaRemote.check(captchaCheckRequest);
        if (!ResultUtil.check(captchaResult) || !Boolean.TRUE.equals(captchaResult.getData())) {
            return requestFail("验证码错误");
        }
        TenantUserDO tenantUser = new TenantUserDO(uid);
        tenantUser.setPhoneNumber(request.getPhoneNumber());
        return toRes(tenantUserService.updateById(tenantUser));
    }

    @Override
    public Result<Void> updatePwd(PasswordUpdateRequest request) {
        Long uid = SecurityUtil.getUid();
        String password = tenantUserService.getById(uid).getPassword();
        if (!passwordEncoder.matches(request.getOldPassword(), password)) {
            return requestFail("修改密码失败，旧密码错误");
        }
        if (passwordEncoder.matches(request.getNewPassword(), password)) {
            return requestFail("新密码不能与旧密码相同");
        }
        TenantUserDO update = new TenantUserDO(uid);
        update.setPassword(passwordEncoder.encode(request.getNewPassword()));
        return toRes(tenantUserService.updateById(update));
    }

}
