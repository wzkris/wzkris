package com.wzkris.usercenter.impl.admin;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wzkris.common.core.constant.SecurityConstants;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.ResultUtil;
import com.wzkris.common.orm.model.AbstractApi;
import com.wzkris.common.security.utils.SecurityUtil;
import com.wzkris.usercenter.api.admin.AdminInfoApi;
import com.wzkris.usercenter.domain.AdminInfoDO;
import com.wzkris.usercenter.mapper.DeptInfoMapper;
import com.wzkris.usercenter.remote.interfaces.captcha.ICaptchaRemote;
import com.wzkris.usercenter.remote.interfaces.captcha.request.CaptchaCheckRequest;
import com.wzkris.usercenter.request.PasswordUpdateRequest;
import com.wzkris.usercenter.request.PhoneNumberUpdateRequest;
import com.wzkris.usercenter.request.admin.AdminInfoBasicUpdateRequest;
import com.wzkris.usercenter.response.admin.AdminInfoResponse;
import com.wzkris.usercenter.response.admin.ChatPersonResponse;
import com.wzkris.usercenter.service.AdminInfoService;
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

    private final DeptInfoMapper deptInfoMapper;

    private final ICaptchaRemote captchaRemote;

    private final PasswordEncoder passwordEncoder;

    @Override
    public Result<AdminInfoResponse> queryInfo() {
        final Long uid = SecurityUtil.getUid();
        boolean issuper = SecurityUtil.isSuper();
        AdminInfoDO adminInfoDO = adminInfoService.getById(uid);
        if (adminInfoDO == null) {
            adminInfoDO = new AdminInfoDO();
        }
        AdminInfoResponse adminInfoVO = new AdminInfoResponse();
        adminInfoVO.setAdmin(issuper);
        adminInfoVO.setUsername(adminInfoDO.getUsername());
        adminInfoVO.setAuthorities(SecurityUtil.getPermission());
        adminInfoVO.setAvatar(adminInfoDO.getAvatar());
        adminInfoVO.setNickname(adminInfoDO.getNickname());
        adminInfoVO.setEmail(adminInfoDO.getEmail());
        adminInfoVO.setPhoneNumber(adminInfoDO.getPhoneNumber());
        adminInfoVO.setGender(adminInfoDO.getGender());
        adminInfoVO.setLoginDate(adminInfoDO.getLoginDate());
        adminInfoVO.setDeptName(deptInfoMapper.selectDeptNameById(adminInfoDO.getDeptId()));
        adminInfoVO.setRoleGroup(issuper ? SecurityConstants.SUPER_ADMIN_NAME : roleInfoService.getRoleGroup(uid));
        return ok(adminInfoVO);
    }

    @Override
    public Result<List<ChatPersonResponse>> queryChatPersonList() {
        List<AdminInfoDO> adminInfoDOS = adminInfoService.list(Wrappers.lambdaQuery(AdminInfoDO.class)
                .select(AdminInfoDO::getAdminId, AdminInfoDO::getNickname, AdminInfoDO::getAvatar)
                .ne(AdminInfoDO::getAdminId, SecurityUtil.getUid()));
        return ok(cast2ChatVO(adminInfoDOS));
    }

    private List<ChatPersonResponse> cast2ChatVO(List<AdminInfoDO> adminInfoDOS) {
        return adminInfoDOS.stream().map(userInfoDO ->
                        new ChatPersonResponse(userInfoDO.getAdminId(), userInfoDO.getNickname(), userInfoDO.getAvatar()))
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
