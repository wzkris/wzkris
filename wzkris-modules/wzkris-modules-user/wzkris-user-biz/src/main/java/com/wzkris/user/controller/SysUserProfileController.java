package com.wzkris.user.controller;

import com.wzkris.auth.rmi.RmiCaptchaFeign;
import com.wzkris.auth.rmi.domain.req.SmsCheckReq;
import com.wzkris.common.core.domain.Result;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateType;
import com.wzkris.common.orm.annotation.IgnoreTenant;
import com.wzkris.common.orm.model.BaseController;
import com.wzkris.common.security.utils.SystemUserUtil;
import com.wzkris.user.domain.SysUser;
import com.wzkris.user.domain.req.EditPhoneReq;
import com.wzkris.user.domain.req.EditPwdReq;
import com.wzkris.user.domain.req.EditSysUserProfileReq;
import com.wzkris.user.domain.vo.SysUserProfileVO;
import com.wzkris.user.mapper.SysDeptMapper;
import com.wzkris.user.mapper.SysUserMapper;
import com.wzkris.user.service.SysRoleService;
import com.wzkris.user.service.SysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 个人信息 业务处理
 *
 * @author wzkris
 */
@Tag(name = "个人信息")
@RestController
@RequestMapping("/user_profile")
@IgnoreTenant(value = false, forceTenantId = "@su.getTenantId()") // 忽略切换
@RequiredArgsConstructor
public class SysUserProfileController extends BaseController {

    private static final String PROFILE_KEY = "user:profile";

    private final SysUserMapper userMapper;

    private final SysUserService userService;

    private final SysRoleService roleService;

    private final SysDeptMapper deptMapper;

    private final RmiCaptchaFeign rmiCaptchaFeign;

    private final PasswordEncoder passwordEncoder;

    @Operation(summary = "账户信息")
    @GetMapping
    @Cacheable(cacheNames = PROFILE_KEY + "#1800#600", key = "@su.getUserId()")
    public Result<SysUserProfileVO> profile() {
        SysUser user = userMapper.selectById(SystemUserUtil.getUserId());

        if (user == null) {// 降级会走到这
            user = new SysUser();
        }
        SysUserProfileVO.UserInfo userInfo = new SysUserProfileVO.UserInfo();
        userInfo.setUsername(user.getUsername());
        userInfo.setAvatar(user.getAvatar());
        userInfo.setNickname(user.getNickname());
        userInfo.setEmail(user.getEmail());
        userInfo.setPhoneNumber(user.getPhoneNumber());
        userInfo.setGender(user.getGender());
        userInfo.setLoginDate(user.getLoginDate());

        SysUserProfileVO profileVO = new SysUserProfileVO();
        profileVO.setUser(userInfo);
        profileVO.setDeptName(deptMapper.selectDeptNameById(user.getDeptId()));
        profileVO.setRoleGroup(roleService.getRoleGroup());
        return ok(profileVO);
    }

    @Operation(summary = "修改基本信息")
    @OperateLog(title = "个人信息", subTitle = "修改基本信息", operateType = OperateType.UPDATE)
    @PostMapping
    @CacheEvict(cacheNames = PROFILE_KEY, key = "@su.getUserId()")
    public Result<Void> editProfile(@RequestBody EditSysUserProfileReq profileReq) {
        SysUser user = new SysUser(SystemUserUtil.getUserId());
        user.setNickname(profileReq.getNickname());
        user.setGender(profileReq.getGender());
        return toRes(userMapper.updateById(user));
    }

    @Operation(summary = "修改手机号")
    @OperateLog(title = "个人信息", subTitle = "修改手机号", operateType = OperateType.UPDATE)
    @PostMapping("/edit_phonenumber")
    @CacheEvict(cacheNames = PROFILE_KEY, key = "@su.getUserId()")
    public Result<Void> editPhoneNumber(@RequestBody @Valid EditPhoneReq req) {
        Long userId = SystemUserUtil.getUserId();

        if (userService.checkExistByPhoneNumber(userId, req.getPhoneNumber())) {
            return err412("该手机号已被使用");
        }
        // 验证
        SmsCheckReq smsCheckReq = new SmsCheckReq(userMapper.selectPhoneNumberById(userId), req.getSmsCode());
        if (!rmiCaptchaFeign.validateSms(smsCheckReq)) {
            return err412("验证码错误");
        }

        SysUser user = new SysUser(userId);
        user.setPhoneNumber(req.getPhoneNumber());
        return toRes(userMapper.updateById(user));
    }

    @Operation(summary = "修改密码")
    @OperateLog(title = "个人信息", subTitle = "修改密码", operateType = OperateType.UPDATE)
    @PostMapping("/edit_password")
    public Result<Void> editPwd(@RequestBody @Validated(EditPwdReq.LoginPwd.class) EditPwdReq req) {
        Long userId = SystemUserUtil.getUserId();

        String password = userMapper.selectPwdById(userId);

        if (!passwordEncoder.matches(req.getOldPassword(), password)) {
            return err412("修改密码失败，旧密码错误");
        }

        if (passwordEncoder.matches(req.getNewPassword(), password)) {
            return err412("新密码不能与旧密码相同");
        }

        SysUser update = new SysUser(userId);
        update.setPassword(passwordEncoder.encode(req.getNewPassword()));
        return toRes(userMapper.updateById(update));
    }

    @Operation(summary = "更新头像")
    @OperateLog(title = "个人信息", subTitle = "更新头像", operateType = OperateType.UPDATE)
    @PostMapping("/edit_avatar")
    @CacheEvict(cacheNames = PROFILE_KEY, key = "@su.getUserId()")
    public Result<Void> updateAvatar(@RequestBody String url) {
        SysUser sysUser = new SysUser(SystemUserUtil.getUserId());
        sysUser.setAvatar(url);
        return toRes(userMapper.updateById(sysUser));
    }

}
