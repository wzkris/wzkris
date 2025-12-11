package com.wzkris.principal.controller.member;

import com.wzkris.auth.httpservice.captcha.CaptchaHttpService;
import com.wzkris.auth.httpservice.captcha.req.CaptchaCheckReq;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateTypeEnum;
import com.wzkris.common.orm.model.BaseController;
import com.wzkris.common.security.utils.TenantUtil;
import com.wzkris.principal.domain.MemberInfoDO;
import com.wzkris.principal.domain.req.EditPhoneReq;
import com.wzkris.principal.domain.req.EditPwdReq;
import com.wzkris.principal.domain.req.member.MemberInfoReq;
import com.wzkris.principal.domain.vo.member.MemberInfoVO;
import com.wzkris.principal.mapper.MemberInfoMapper;
import com.wzkris.principal.service.MemberInfoService;
import com.wzkris.principal.service.PostInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "租户成员信息")
@Validated
@RestController
@RequestMapping("/member-info")
@RequiredArgsConstructor
public class MemberInfoController extends BaseController {

    private final MemberInfoMapper memberInfoMapper;

    private final MemberInfoService memberInfoService;

    private final PostInfoService postInfoService;

    private final CaptchaHttpService captchaHttpService;

    private final PasswordEncoder passwordEncoder;

    @Operation(summary = "账户信息")
    @GetMapping
    public Result<MemberInfoVO> userinfo() {
        MemberInfoDO member = memberInfoMapper.selectById(TenantUtil.getId());

        if (member == null) {// 降级会走到这
            member = new MemberInfoDO();
        }
        MemberInfoVO memberInfoVO = new MemberInfoVO();
        memberInfoVO.setAdmin(TenantUtil.isAdmin());
        memberInfoVO.setUsername(TenantUtil.getUsername());
        memberInfoVO.setAuthorities(TenantUtil.getAuthorities());
        memberInfoVO.setAvatar(member.getAvatar());
        memberInfoVO.setPhoneNumber(member.getPhoneNumber());
        memberInfoVO.setGender(member.getGender());
        memberInfoVO.setLoginDate(member.getLoginDate());

        memberInfoVO.setPostGroup(postInfoService.getPostGroup());
        return ok(memberInfoVO);
    }

    @Operation(summary = "修改基本信息")
    @OperateLog(title = "个人信息", subTitle = "修改基本信息", type = OperateTypeEnum.UPDATE)
    @PostMapping
    public Result<Void> editInfo(@RequestBody MemberInfoReq profileReq) {
        MemberInfoDO memberInfoDO = new MemberInfoDO(TenantUtil.getId());
        memberInfoDO.setGender(profileReq.getGender());
        return toRes(memberInfoMapper.updateById(memberInfoDO));
    }

    @Operation(summary = "修改手机号")
    @OperateLog(title = "个人信息", subTitle = "修改手机号", type = OperateTypeEnum.UPDATE)
    @PostMapping("/edit-phonenumber")
    public Result<Void> editPhoneNumber(@RequestBody @Valid EditPhoneReq req) {
        Long memberId = TenantUtil.getId();

        if (memberInfoService.existByPhoneNumber(memberId, req.getPhoneNumber())) {
            return err40000("该手机号已被使用");
        }
        // 验证
        CaptchaCheckReq captchaCheckReq = new CaptchaCheckReq(memberInfoMapper.selectPhoneNumberById(memberId), req.getSmsCode());
        if (!captchaHttpService.validateCaptcha(captchaCheckReq)) {
            return err40000("验证码错误");
        }

        MemberInfoDO member = new MemberInfoDO(memberId);
        member.setPhoneNumber(req.getPhoneNumber());
        return toRes(memberInfoMapper.updateById(member));
    }

    @Operation(summary = "修改密码")
    @OperateLog(title = "个人信息", subTitle = "修改密码", type = OperateTypeEnum.UPDATE)
    @PostMapping("/edit-password")
    public Result<Void> editPwd(@RequestBody @Validated(EditPwdReq.LoginPwd.class) EditPwdReq req) {
        Long memberId = TenantUtil.getId();

        String password = memberInfoMapper.selectPwdById(memberId);

        if (!passwordEncoder.matches(req.getOldPassword(), password)) {
            return err40000("修改密码失败，旧密码错误");
        }

        if (passwordEncoder.matches(req.getNewPassword(), password)) {
            return err40000("新密码不能与旧密码相同");
        }

        MemberInfoDO update = new MemberInfoDO(memberId);
        update.setPassword(passwordEncoder.encode(req.getNewPassword()));
        return toRes(memberInfoMapper.updateById(update));
    }

    @Operation(summary = "更新头像")
    @OperateLog(title = "个人信息", subTitle = "更新头像", type = OperateTypeEnum.UPDATE)
    @PostMapping("/edit-avatar")
    public Result<Void> editAvatar(@RequestBody String url) {
        MemberInfoDO memberInfoDO = new MemberInfoDO(TenantUtil.getId());
        memberInfoDO.setAvatar(url);
        return toRes(memberInfoMapper.updateById(memberInfoDO));
    }

}
