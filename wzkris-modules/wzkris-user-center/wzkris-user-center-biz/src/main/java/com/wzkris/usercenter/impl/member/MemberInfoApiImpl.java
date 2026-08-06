package com.wzkris.usercenter.impl.member;

import com.wzkris.common.core.support.LoginUser;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.model.UserRole;
import com.wzkris.common.core.utils.ResultUtil;
import com.wzkris.common.security.utils.SecurityUtil;
import com.wzkris.common.web.model.AbstractApi;
import com.wzkris.usercenter.api.member.MemberInfoApi;
import com.wzkris.usercenter.api.member.request.MemberInfoBasicUpdateRequest;
import com.wzkris.usercenter.api.member.response.MemberInfoQueryResponse;
import com.wzkris.usercenter.domain.MemberInfoDO;
import com.wzkris.usercenter.remote.interfaces.captcha.ICaptchaRemote;
import com.wzkris.usercenter.remote.interfaces.captcha.request.CaptchaCheckRequest;
import com.wzkris.usercenter.request.PasswordUpdateRequest;
import com.wzkris.usercenter.request.PhoneNumberUpdateRequest;
import com.wzkris.usercenter.service.MemberInfoService;
import com.wzkris.usercenter.service.PostInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberInfoApiImpl extends AbstractApi implements MemberInfoApi {

    private final MemberInfoService memberInfoService;

    private final PostInfoService postInfoService;

    private final ICaptchaRemote captchaRemote;

    private final PasswordEncoder passwordEncoder;

    @Override
    public Result<MemberInfoQueryResponse> queryInfo() {
        LoginUser loginUser = SecurityUtil.getLoginUser();
        MemberInfoDO member = memberInfoService.getById(loginUser.getUid());
        MemberInfoQueryResponse memberInfoVO = new MemberInfoQueryResponse();
        memberInfoVO.setAdmin(SecurityUtil.isSuperUser());
        memberInfoVO.setUsername(member.getUsername());
        memberInfoVO.setAuthorities(SecurityUtil.getPermission());
        memberInfoVO.setAvatar(member.getAvatar());
        memberInfoVO.setPhoneNumber(member.getPhoneNumber());
        memberInfoVO.setGender(member.getGender());
        memberInfoVO.setLoginDate(member.getLoginDate());
        memberInfoVO.setPostGroup(SecurityUtil.getRoleContext().getRoles().stream()
                .map(UserRole::getName).collect(Collectors.joining(",")));
        return ok(memberInfoVO);
    }

    @Override
    public Result<Void> updateBasicInfo(MemberInfoBasicUpdateRequest request) {
        MemberInfoDO memberInfoDO = new MemberInfoDO(SecurityUtil.getUid());
        memberInfoDO.setGender(request.getGender());
        memberInfoDO.setAvatar(request.getAvatar());
        return toRes(memberInfoService.updateById(memberInfoDO));
    }

    @Override
    public Result<Void> updatePhoneNumber(PhoneNumberUpdateRequest request) {
        Long uid = SecurityUtil.getUid();
        if (memberInfoService.existByPhoneNumber(uid, request.getPhoneNumber())) {
            return requestFail("该手机号已被使用");
        }
        CaptchaCheckRequest captchaCheckRequest = new CaptchaCheckRequest();
        MemberInfoDO memberInfoDO = memberInfoService.getById(uid);
        captchaCheckRequest.setKey(memberInfoDO.getPhoneNumber());
        captchaCheckRequest.setValue(request.getSmsCode());
        Result<Boolean> captchaResult = captchaRemote.check(captchaCheckRequest);
        if (!ResultUtil.check(captchaResult) || !Boolean.TRUE.equals(captchaResult.getData())) {
            return requestFail("验证码错误");
        }
        MemberInfoDO member = new MemberInfoDO(uid);
        member.setPhoneNumber(request.getPhoneNumber());
        return toRes(memberInfoService.updateById(member));
    }

    @Override
    public Result<Void> updatePwd(PasswordUpdateRequest request) {
        Long uid = SecurityUtil.getUid();
        String password = memberInfoService.getById(uid).getPassword();
        if (!passwordEncoder.matches(request.getOldPassword(), password)) {
            return requestFail("修改密码失败，旧密码错误");
        }
        if (passwordEncoder.matches(request.getNewPassword(), password)) {
            return requestFail("新密码不能与旧密码相同");
        }
        MemberInfoDO update = new MemberInfoDO(uid);
        update.setPassword(passwordEncoder.encode(request.getNewPassword()));
        return toRes(memberInfoService.updateById(update));
    }

}
