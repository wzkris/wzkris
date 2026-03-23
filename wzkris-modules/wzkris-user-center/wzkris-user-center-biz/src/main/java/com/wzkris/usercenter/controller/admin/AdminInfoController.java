package com.wzkris.usercenter.controller.admin;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wzkris.captcha.httpclient.common.CaptchaClient;
import com.wzkris.captcha.httpclient.common.req.CaptchaCheckReq;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.ResultUtil;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateTypeEnum;
import com.wzkris.common.orm.model.BaseController;
import com.wzkris.common.security.utils.SecurityUtil;
import com.wzkris.usercenter.domain.AdminInfoDO;
import com.wzkris.usercenter.domain.req.PhoneEditReq;
import com.wzkris.usercenter.domain.req.PwdEditReq;
import com.wzkris.usercenter.domain.req.admin.AdminInfoEditReq;
import com.wzkris.usercenter.domain.resp.admin.AdminInfoResp;
import com.wzkris.usercenter.domain.resp.admin.ChatPersonResp;
import com.wzkris.usercenter.mapper.AdminInfoMapper;
import com.wzkris.usercenter.mapper.DeptInfoMapper;
import com.wzkris.usercenter.service.AdminInfoService;
import com.wzkris.usercenter.service.RoleInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 个人信息 业务处理
 *
 * @author wzkris
 */
@Tag(name = "管理员信息")
@RestController
@RequestMapping("/admin-info")
@RequiredArgsConstructor
public class AdminInfoController extends BaseController {

    private final String info_prefix = "userinfo";

    private final AdminInfoMapper adminInfoMapper;

    private final AdminInfoService adminInfoService;

    private final RoleInfoService roleInfoService;

    private final DeptInfoMapper deptInfoMapper;

    private final CaptchaClient captchaClient;

    private final PasswordEncoder passwordEncoder;

    @Operation(summary = "账户信息")
    @GetMapping
    @Cacheable(value = info_prefix + "#600_000", key = "@su.getUid()", sync = true) // TODO 这里缓存的需要在退出时移除
    public Result<AdminInfoResp> userinfo() {
        AdminInfoDO adminInfoDO = adminInfoMapper.selectById(SecurityUtil.getUid());

        if (adminInfoDO == null) {// 降级会走到这
            adminInfoDO = new AdminInfoDO();
        }
        AdminInfoResp adminInfoVO = new AdminInfoResp();
        adminInfoVO.setAdmin(SecurityUtil.isSuper());
        adminInfoVO.setUsername(adminInfoDO.getUsername());
        adminInfoVO.setAuthorities(SecurityUtil.getPermission());
        adminInfoVO.setAvatar(adminInfoDO.getAvatar());
        adminInfoVO.setNickname(adminInfoDO.getNickname());
        adminInfoVO.setEmail(adminInfoDO.getEmail());
        adminInfoVO.setPhoneNumber(adminInfoDO.getPhoneNumber());
        adminInfoVO.setGender(adminInfoDO.getGender());
        adminInfoVO.setLoginDate(adminInfoDO.getLoginDate());

        adminInfoVO.setDeptName(deptInfoMapper.selectDeptNameById(adminInfoDO.getDeptId()));
        adminInfoVO.setRoleGroup(roleInfoService.getRoleGroup());
        return ok(adminInfoVO);
    }

    @Operation(summary = "聊天人员列表")
    @GetMapping("/chat-person-list")
    public Result<List<ChatPersonResp>> chatPersonList() {
        List<AdminInfoDO> adminInfoDOS = adminInfoMapper.selectList(Wrappers.lambdaQuery(AdminInfoDO.class)
                .select(AdminInfoDO::getAdminId, AdminInfoDO::getNickname, AdminInfoDO::getAvatar)
                .ne(AdminInfoDO::getAdminId, SecurityUtil.getUid()));

        return ok(cast2ChatVO(adminInfoDOS));
    }

    private List<ChatPersonResp> cast2ChatVO(List<AdminInfoDO> adminInfoDOS) {
        return adminInfoDOS.stream().map(userInfoDO ->
                        new ChatPersonResp(userInfoDO.getAdminId(), userInfoDO.getNickname(), userInfoDO.getAvatar()))
                .collect(Collectors.toList());
    }

    @Operation(summary = "修改基本信息")
    @OperateLog(title = "个人信息", subTitle = "修改基本信息", type = OperateTypeEnum.UPDATE)
    @PostMapping
    @CacheEvict(value = info_prefix, key = "@su.getUid()")
    public Result<Void> editInfo(@RequestBody AdminInfoEditReq req) {
        AdminInfoDO admin = new AdminInfoDO(SecurityUtil.getUid());
        admin.setNickname(req.getNickname());
        admin.setGender(req.getGender());
        return toRes(adminInfoMapper.updateById(admin));
    }

    @Operation(summary = "修改手机号")
    @OperateLog(title = "个人信息", subTitle = "修改手机号", type = OperateTypeEnum.UPDATE)
    @PostMapping("/edit-phonenumber")
    @CacheEvict(value = info_prefix, key = "@su.getUid()")
    public Result<Void> editPhoneNumber(@RequestBody @Valid PhoneEditReq req) {
        Long adminId = SecurityUtil.getUid();

        if (adminInfoService.existByPhoneNumber(adminId, req.getPhoneNumber())) {
            return requestFail("该手机号已被使用");
        }
        // 验证
        CaptchaCheckReq captchaCheckReq = new CaptchaCheckReq();
        captchaCheckReq.setKey(adminInfoMapper.selectPhoneNumberById(adminId));
        captchaCheckReq.setValue(req.getSmsCode());
        Result<Boolean> captchaResult = captchaClient.check(captchaCheckReq);
        if (!ResultUtil.check(captchaResult) || !Boolean.TRUE.equals(captchaResult.getData())) {
            return requestFail("验证码错误");
        }

        AdminInfoDO admin = new AdminInfoDO(adminId);
        admin.setPhoneNumber(req.getPhoneNumber());
        return toRes(adminInfoMapper.updateById(admin));
    }

    @Operation(summary = "修改密码")
    @OperateLog(title = "个人信息", subTitle = "修改密码", type = OperateTypeEnum.UPDATE)
    @PostMapping("/edit-password")
    public Result<Void> editPwd(@RequestBody @Validated(PwdEditReq.LoginPwd.class) PwdEditReq req) {
        Long adminId = SecurityUtil.getUid();

        String password = adminInfoMapper.selectPwdById(adminId);

        if (!passwordEncoder.matches(req.getOldPassword(), password)) {
            return requestFail("修改密码失败，旧密码错误");
        }

        if (passwordEncoder.matches(req.getNewPassword(), password)) {
            return requestFail("新密码不能与旧密码相同");
        }

        AdminInfoDO update = new AdminInfoDO(adminId);
        update.setPassword(passwordEncoder.encode(req.getNewPassword()));
        return toRes(adminInfoMapper.updateById(update));
    }

    @Operation(summary = "更新头像")
    @OperateLog(title = "个人信息", subTitle = "更新头像", type = OperateTypeEnum.UPDATE)
    @PostMapping("/edit-avatar")
    @CacheEvict(value = info_prefix, key = "@su.getUid()")
    public Result<Void> editAvatar(@RequestBody String url) {
        AdminInfoDO adminInfoDO = new AdminInfoDO(SecurityUtil.getUid());
        adminInfoDO.setAvatar(url);
        return toRes(adminInfoMapper.updateById(adminInfoDO));
    }

}
