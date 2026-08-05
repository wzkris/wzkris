package com.wzkris.usercenter.impl.admin;

import com.wzkris.common.core.support.LoginUser;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.model.UserRole;
import com.wzkris.common.core.utils.ResultUtil;
import com.wzkris.common.security.utils.SecurityUtil;
import com.wzkris.common.web.model.AbstractApi;
import com.wzkris.usercenter.api.admin.AdminInfoApi;
import com.wzkris.usercenter.api.admin.request.AdminInfoBasicUpdateRequest;
import com.wzkris.usercenter.api.admin.response.AdminInfoResponse;
import com.wzkris.usercenter.api.admin.response.ChatPersonResponse;
import com.wzkris.usercenter.domain.AdminInfoDO;
import com.wzkris.usercenter.domain.DeptInfoDO;
import com.wzkris.usercenter.remote.interfaces.captcha.ICaptchaRemote;
import com.wzkris.usercenter.remote.interfaces.captcha.request.CaptchaCheckRequest;
import com.wzkris.usercenter.request.PasswordUpdateRequest;
import com.wzkris.usercenter.request.PhoneNumberUpdateRequest;
import com.wzkris.usercenter.service.AdminInfoService;
import com.wzkris.usercenter.service.DeptInfoService;
import com.wzkris.usercenter.service.RoleInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminInfoApiImpl extends AbstractApi implements AdminInfoApi {

    private final AdminInfoService adminInfoService;

    private final RoleInfoService roleInfoService;

    private final DeptInfoService deptInfoService;

    private final ICaptchaRemote captchaRemote;

    private final PasswordEncoder passwordEncoder;

    @Override
    public Result<AdminInfoResponse> queryInfo() {
        LoginUser loginUser = SecurityUtil.getLoginUser();
        AdminInfoDO adminInfoDO = adminInfoService.getById(loginUser.getUid());
        AdminInfoResponse adminInfoVO = new AdminInfoResponse();
        adminInfoVO.setAdmin(SecurityUtil.getRoleContext().isSuperUser());
        adminInfoVO.setUsername(adminInfoDO.getUsername());
        adminInfoVO.setAuthorities(SecurityUtil.getPermission());
        adminInfoVO.setAvatar(adminInfoDO.getAvatar());
        adminInfoVO.setNickname(adminInfoDO.getNickname());
        adminInfoVO.setEmail(adminInfoDO.getEmail());
        adminInfoVO.setPhoneNumber(adminInfoDO.getPhoneNumber());
        adminInfoVO.setGender(adminInfoDO.getGender());
        adminInfoVO.setLoginDate(adminInfoDO.getLoginDate());
        DeptInfoDO deptInfoDO = deptInfoService.getById(adminInfoDO.getDeptId());
        adminInfoVO.setDeptName(deptInfoDO == null ? "" : deptInfoDO.getDeptName());
        adminInfoVO.setRoleGroup(SecurityUtil.getRoleContext().getRoles().stream()
                .map(UserRole::getName).collect(Collectors.joining(",")));
        return ok(adminInfoVO);
    }

    private List<ChatPersonResponse> cast2ChatVO(List<AdminInfoDO> adminInfoDOS) {
        return adminInfoDOS.stream().map(userInfoDO ->
                        new ChatPersonResponse(userInfoDO.getId(), userInfoDO.getNickname(), userInfoDO.getAvatar()))
                .collect(Collectors.toList());
    }

    @Override
    public Result<Void> updateBasicInfo(AdminInfoBasicUpdateRequest request) {
        AdminInfoDO adminInfoDO = new AdminInfoDO(SecurityUtil.getUid());
        adminInfoDO.setNickname(request.getNickname());
        adminInfoDO.setGender(request.getGender());
        adminInfoDO.setAvatar(request.getAvatar());
        return toRes(adminInfoService.updateById(adminInfoDO));
    }

    @Override
    public Result<Void> updatePhoneNumber(PhoneNumberUpdateRequest request) {
        Long uid = SecurityUtil.getUid();
        if (adminInfoService.existByPhoneNumber(uid, request.getPhoneNumber())) {
            return requestFail("该手机号已被使用");
        }
        CaptchaCheckRequest captchaCheckRequest = new CaptchaCheckRequest();
        captchaCheckRequest.setKey(adminInfoService.getById(uid).getPhoneNumber());
        captchaCheckRequest.setValue(request.getSmsCode());
        Result<Boolean> captchaResult = captchaRemote.check(captchaCheckRequest);
        if (!ResultUtil.check(captchaResult) || !Boolean.TRUE.equals(captchaResult.getData())) {
            return requestFail("验证码错误");
        }
        AdminInfoDO admin = new AdminInfoDO(uid);
        admin.setPhoneNumber(request.getPhoneNumber());
        return toRes(adminInfoService.updateById(admin));
    }

    @Override
    public Result<Void> updatePwd(PasswordUpdateRequest request) {
        Long uid = SecurityUtil.getUid();
        String password = adminInfoService.getById(uid).getPassword();
        if (!passwordEncoder.matches(request.getOldPassword(), password)) {
            return requestFail("修改密码失败，旧密码错误");
        }
        if (passwordEncoder.matches(request.getNewPassword(), password)) {
            return requestFail("新密码不能与旧密码相同");
        }
        AdminInfoDO update = new AdminInfoDO(uid);
        update.setPassword(passwordEncoder.encode(request.getNewPassword()));
        return toRes(adminInfoService.updateById(update));
    }

}
