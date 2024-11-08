package com.wzkris.user.controller;

import cn.hutool.core.map.MapUtil;
import com.wzkris.auth.api.RemoteCaptchaApi;
import com.wzkris.auth.api.domain.SmsDTO;
import com.wzkris.common.core.domain.Result;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateType;
import com.wzkris.common.security.oauth2.domain.model.LoginSyser;
import com.wzkris.common.security.utils.SysUtil;
import com.wzkris.common.web.model.BaseController;
import com.wzkris.user.domain.SysUser;
import com.wzkris.user.domain.dto.EditPasswordDTO;
import com.wzkris.user.domain.dto.EditPhoneDTO;
import com.wzkris.user.domain.dto.SysUserDTO;
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

import java.util.Map;

/**
 * 个人信息 业务处理
 *
 * @author wzkris
 */
@Tag(name = "系统账户信息")
@RestController
@RequestMapping("/user/account")
@RequiredArgsConstructor
public class SysUserOwnController extends BaseController {
    private final SysUserMapper userMapper;
    private final SysUserService userService;
    private final SysRoleService roleService;
    private final SysPostService postService;
    private final SysDeptMapper deptMapper;
    private final RemoteCaptchaApi remoteCaptchaApi;
    private final PasswordEncoder passwordEncoder;

    @Operation(summary = "个人信息")
    @GetMapping
    public Result<?> info() {
        SysUser sysUser = userMapper.selectById(SysUtil.getUserId());
        Map<String, Object> userMap =
                MapUtil.newHashMap(4);
        userMap.put(SysUser.Fields.username, sysUser.getUsername());
        userMap.put(SysUser.Fields.nickname, sysUser.getNickname());
        userMap.put(SysUser.Fields.phoneNumber, sysUser.getPhoneNumber());
        userMap.put(SysUser.Fields.email, sysUser.getEmail());
        // 返回的map
        Map<String, Object> res = MapUtil.newHashMap(4);
        res.put("user", userMap);
        res.put("deptName", StringUtil.emptyToDefault(deptMapper.selectDeptNameById(sysUser.getDeptId()), StringUtil.EMPTY));
        res.put("roleGroup", roleService.getRoleGroup(sysUser.getUserId()));
        res.put("postGroup", postService.getPostGroup(sysUser.getUserId()));
        return ok(res);
    }

    @Operation(summary = "修改昵称、性别")
    @OperateLog(title = "个人中心", operateType = OperateType.UPDATE)
    @PostMapping
    public Result<?> editInfo(@RequestBody SysUserDTO sysUserDTO) {
        SysUser user = new SysUser(SysUtil.getUserId());
        user.setNickname(sysUserDTO.getNickname());
        user.setGender(sysUserDTO.getGender());
        return toRes(userMapper.updateById(user));
    }

    @Operation(summary = "修改手机号")
    @OperateLog(title = "个人中心", operateType = OperateType.UPDATE)
    @PostMapping("/edit_phonenumber")
    public Result<?> editPhoneNumber(@RequestBody @Valid EditPhoneDTO phoneDTO) {
        Long userId = SysUtil.getUserId();

        if (userService.checkUserUnique(new SysUser(userId).setPhoneNumber(phoneDTO.getPhoneNumber()))) {
            return fail("该手机号已被使用");
        }
        // 验证
        SmsDTO smsDTO = new SmsDTO(userMapper.selectPhoneNumberById(userId), phoneDTO.getSmsCode());
        Result<Void> result = remoteCaptchaApi.validateSms(smsDTO);
        result.checkData();

        SysUser user = new SysUser(userId);
        user.setPhoneNumber(phoneDTO.getPhoneNumber());
        return toRes(userMapper.updateById(user));
    }

    @Operation(summary = "修改密码")
    @OperateLog(title = "个人中心", operateType = OperateType.UPDATE)
    @PostMapping("/edit_password")
    public Result<?> editPwd(@RequestBody @Valid EditPasswordDTO passwordDTO) {
        LoginSyser loginUser = SysUtil.getLoginSyser();

        String username = loginUser.getUsername();
        String password = userMapper.selectPwdByUserName(username);

        if (!passwordEncoder.matches(passwordDTO.getOldPassword(), password)) {
            return fail("修改密码失败，旧密码错误");
        }

        if (passwordEncoder.matches(passwordDTO.getNewPassword(), password)) {
            return fail("新密码不能与旧密码相同");
        }

        SysUser update = new SysUser(loginUser.getUserId());
        update.setPassword(passwordEncoder.encode(password));
        return toRes(userMapper.updateById(update));
    }

    @Operation(summary = "更新头像")
    @OperateLog(title = "个人中心", operateType = OperateType.UPDATE)
    @PostMapping("/edit_avatar")
    public Result<?> updateAvatar(@RequestBody String url) {
        SysUser sysUser = new SysUser(SysUtil.getUserId());
        sysUser.setAvatar(url);
        return toRes(userMapper.updateById(sysUser));
    }
}
