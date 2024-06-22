package com.thingslink.user.controller;

import cn.hutool.core.map.MapUtil;
import com.thingslink.auth.api.RemoteCaptchaApi;
import com.thingslink.auth.api.domain.SmsDTO;
import com.thingslink.common.core.domain.Result;
import com.thingslink.common.core.utils.StringUtil;
import com.thingslink.common.log.annotation.OperateLog;
import com.thingslink.common.log.enums.OperateType;
import com.thingslink.common.orm.model.BaseController;
import com.thingslink.common.security.oauth2.domain.model.LoginSyser;
import com.thingslink.common.security.utils.SysUtil;
import com.thingslink.user.domain.SysUser;
import com.thingslink.user.domain.dto.EditPasswordDTO;
import com.thingslink.user.domain.dto.EditPhoneDTO;
import com.thingslink.user.domain.dto.SysUserDTO;
import com.thingslink.user.mapper.SysDeptMapper;
import com.thingslink.user.mapper.SysUserMapper;
import com.thingslink.user.service.SysPostService;
import com.thingslink.user.service.SysRoleService;
import com.thingslink.user.service.SysUserService;
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
@Tag(name = "个人中心")
@RestController
@RequestMapping("/sys_user/personal_center")
@RequiredArgsConstructor
public class SysProfileController extends BaseController {
    private final SysUserMapper userMapper;
    private final SysUserService userService;
    private final SysRoleService sysRoleService;
    private final SysPostService sysPostService;
    private final SysDeptMapper sysDeptMapper;
    private final RemoteCaptchaApi remoteCaptchaApi;
    private final PasswordEncoder passwordEncoder;

    @Operation(summary = "个人信息")
    @GetMapping
    public Result<?> info() {
        SysUser sysUser = userMapper.selectById(SysUtil.getUserId());
        Map<String, Object> userMap = MapUtil.newHashMap(4);
        userMap.put(SysUser.Fields.username, sysUser.getUsername());
        userMap.put(SysUser.Fields.nickname, sysUser.getNickname());
        userMap.put(SysUser.Fields.phoneNumber, sysUser.getPhoneNumber());
        userMap.put(SysUser.Fields.email, sysUser.getEmail());
        // 返回的map
        Map<String, Object> res = MapUtil.newHashMap(4);
        res.put("user", userMap);
        res.put("deptName", StringUtil.emptyToDefault(sysDeptMapper.selectDeptNameById(sysUser.getDeptId()), StringUtil.EMPTY));
        res.put("roleGroup", sysRoleService.getRoleGroup(sysUser.getUserId()));
        res.put("postGroup", sysPostService.getPostGroup(sysUser.getUserId()));
        return success(res);
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

        if (userService.checkUserUnique(new SysUser().setPhoneNumber(phoneDTO.getPhoneNumber()), userId)) {
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
        LoginSyser loginSyser = SysUtil.getLoginSyser();
        return toRes(userMapper.updateAvatar(loginSyser.getUsername(), url) > 0);
    }
}
