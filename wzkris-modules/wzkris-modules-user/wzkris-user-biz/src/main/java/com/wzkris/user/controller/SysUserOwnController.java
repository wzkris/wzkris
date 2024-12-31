package com.wzkris.user.controller;

import com.wzkris.auth.api.RemoteCaptchaApi;
import com.wzkris.auth.api.domain.request.SmsCheckReq;
import com.wzkris.common.core.domain.Result;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateType;
import com.wzkris.common.orm.annotation.IgnoreTenant;
import com.wzkris.common.security.oauth2.domain.model.LoginUser;
import com.wzkris.common.security.utils.LoginUserUtil;
import com.wzkris.common.web.model.BaseController;
import com.wzkris.user.domain.SysUser;
import com.wzkris.user.domain.req.EditOwnSysUserReq;
import com.wzkris.user.domain.req.EditPhoneReq;
import com.wzkris.user.domain.req.EditPwdReq;
import com.wzkris.user.domain.vo.SysUserAccountVO;
import com.wzkris.user.domain.vo.SysUserOwnVO;
import com.wzkris.user.mapper.SysDeptMapper;
import com.wzkris.user.mapper.SysUserMapper;
import com.wzkris.user.service.SysPostService;
import com.wzkris.user.service.SysRoleService;
import com.wzkris.user.service.SysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

/**
 * 个人信息 业务处理
 *
 * @author wzkris
 */
@Tag(name = "系统账户")
@RestController
@RequestMapping("/user")
@IgnoreTenant(value = false, forceTenantId = "@lg.getTenantId()")
@RequiredArgsConstructor
public class SysUserOwnController extends BaseController {
    private final SysUserMapper userMapper;
    private final SysUserService userService;
    private final SysRoleService roleService;
    private final SysPostService postService;
    private final SysDeptMapper deptMapper;
    private final RemoteCaptchaApi remoteCaptchaApi;
    private final PasswordEncoder passwordEncoder;

    @Operation(summary = "登录信息")
    @GetMapping("/info")
    public Result<SysUserOwnVO> loginUser() {
        LoginUser loginUser = LoginUserUtil.getLoginUser();
        SysUserOwnVO userOwnVO = new SysUserOwnVO();
        userOwnVO.setAdmin(loginUser.getAdmin());
        userOwnVO.setUsername(loginUser.getUsername());
        userOwnVO.setAuthorities(LoginUserUtil.getAuthorities());
        return ok(userOwnVO);
    }

    @Operation(summary = "账户信息")
    @GetMapping("/account")
    public Result<SysUserAccountVO> accountVO() {
        SysUser sysUser = userMapper.selectById(LoginUserUtil.getUserId());

        SysUserAccountVO.UserInfo userInfo = new SysUserAccountVO.UserInfo();
        userInfo.setUsername(sysUser.getUsername());
        userInfo.setAvatar(sysUser.getAvatar());
        userInfo.setNickname(sysUser.getNickname());
        userInfo.setEmail(sysUser.getEmail());
        userInfo.setPhoneNumber(sysUser.getPhoneNumber());
        userInfo.setGender(sysUser.getGender());

        SysUserAccountVO accountVO = new SysUserAccountVO();
        accountVO.setUser(userInfo);
        accountVO.setDeptName(deptMapper.selectDeptNameById(sysUser.getDeptId()));
        accountVO.setRoleGroup(roleService.getRoleGroup());
        accountVO.setPostGroup(postService.getPostGroup());
        return ok(accountVO);
    }

    @Operation(summary = "修改昵称、性别")
    @OperateLog(title = "系统账户", operateType = OperateType.UPDATE)
    @PostMapping("/account")
    public Result<Void> editInfo(@RequestBody EditOwnSysUserReq req) {
        SysUser user = new SysUser(LoginUserUtil.getUserId());
        user.setNickname(req.getNickname());
        user.setGender(req.getGender());
        return toRes(userMapper.updateById(user));
    }

    @Operation(summary = "修改手机号")
    @OperateLog(title = "系统账户", operateType = OperateType.UPDATE)
    @PostMapping("/account/edit_phonenumber")
    public Result<Void> editPhoneNumber(@RequestBody @Valid EditPhoneReq req) {
        Long userId = LoginUserUtil.getUserId();

        if (userService.checkUsedByPhoneNumber(userId, req.getPhoneNumber())) {
            return fail("该手机号已被使用");
        }
        // 验证
        SmsCheckReq smsCheckReq = new SmsCheckReq(userMapper.selectPhoneNumberById(userId), req.getSmsCode());
        Result<Void> result = remoteCaptchaApi.validateSms(smsCheckReq);
        result.checkData();

        SysUser user = new SysUser(userId);
        user.setPhoneNumber(req.getPhoneNumber());
        return toRes(userMapper.updateById(user));
    }

    @Operation(summary = "修改密码")
    @OperateLog(title = "系统账户", operateType = OperateType.UPDATE)
    @PostMapping("/account/edit_password")
    public Result<Void> editPwd(@RequestBody @Valid EditPwdReq req) {
        LoginUser loginUser = LoginUserUtil.getLoginUser();

        String username = loginUser.getUsername();
        String password = userMapper.selectPwdByUserName(username);

        if (!passwordEncoder.matches(req.getOldPassword(), password)) {
            return fail("修改密码失败，旧密码错误");
        }

        if (passwordEncoder.matches(req.getNewPassword(), password)) {
            return fail("新密码不能与旧密码相同");
        }

        SysUser update = new SysUser(loginUser.getUserId());
        update.setPassword(passwordEncoder.encode(password));
        return toRes(userMapper.updateById(update));
    }

    @Operation(summary = "更新头像")
    @OperateLog(title = "系统账户", operateType = OperateType.UPDATE)
    @PostMapping("/account/edit_avatar")
    public Result<Void> updateAvatar(@RequestBody String url) {
        SysUser sysUser = new SysUser(LoginUserUtil.getUserId());
        sysUser.setAvatar(url);
        return toRes(userMapper.updateById(sysUser));
    }
}
