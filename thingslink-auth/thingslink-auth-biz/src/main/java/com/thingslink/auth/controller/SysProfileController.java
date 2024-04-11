package com.thingslink.auth.controller;

import cn.hutool.core.map.MapUtil;
import com.thingslink.auth.api.RemoteCaptchaApi;
import com.thingslink.auth.api.domain.SmsDTO;
import com.thingslink.auth.domain.SysUser;
import com.thingslink.auth.domain.dto.SysUserDTO;
import com.thingslink.auth.mapper.SysDeptMapper;
import com.thingslink.auth.mapper.SysUserMapper;
import com.thingslink.auth.service.SysPostService;
import com.thingslink.auth.service.SysRoleService;
import com.thingslink.auth.service.SysUserService;
import com.thingslink.common.core.domain.Result;
import com.thingslink.common.core.utils.StringUtil;
import com.thingslink.common.log.annotation.OperateLog;
import com.thingslink.common.log.enums.BusinessType;
import com.thingslink.common.security.model.LoginUser;
import com.thingslink.common.security.utils.LoginUserUtil;
import com.thingslink.common.web.controller.BaseController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 个人信息 业务处理
 *
 * @author wzkris
 */
@Tag(name = "个人中心")
@RestController
@RequestMapping("/user/profile")
@RequiredArgsConstructor
public class SysProfileController extends BaseController {
    private final SysUserMapper userMapper;
    private final SysUserService userService;
    private final SysRoleService sysRoleService;
    private final SysPostService sysPostService;
    private final SysDeptMapper sysDeptMapper;
    private final RemoteCaptchaApi remoteCaptchaApi;

    @Operation(summary = "个人信息")
    @GetMapping
    public Result<?> profile() {
        SysUser sysUser = userMapper.selectById(LoginUserUtil.getUserId());
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
    @OperateLog(title = "个人中心", businessType = BusinessType.UPDATE)
    @PutMapping
    public Result<?> update(@RequestBody SysUserDTO sysUserDTO) {
        SysUser user = new SysUser();
        user.setUserId(LoginUserUtil.getUserId());
        user.setNickname(sysUserDTO.getNickname());
        user.setGender(sysUserDTO.getGender());
        return toRes(userMapper.updateById(user));
    }

    @Operation(summary = "修改手机号")
    @OperateLog(title = "个人中心", businessType = BusinessType.UPDATE)
    @PutMapping("/phonenumber")
    public Result<?> updatePhoneNumber(String phoneNumber, String smsCode) {
        Long userId = LoginUserUtil.getUserId();

        if (userService.checkUserUnique(new SysUser().setPhoneNumber(phoneNumber), userId)) {
            return fail("该手机号已被使用");
        }
        // 验证
        SmsDTO smsDTO = new SmsDTO(userMapper.selectPhoneNumberById(userId), smsCode);
        remoteCaptchaApi.validateSms(smsDTO);

        SysUser user = new SysUser(userId);
        user.setPhoneNumber(phoneNumber);
        return toRes(userMapper.updateById(user));
    }

    @Operation(summary = "修改密码")
    @OperateLog(title = "个人中心", businessType = BusinessType.UPDATE)
    @PutMapping("/password")
    public Result<?> updatePwd(String oldPassword, String newPassword) {
        LoginUser loginUser = LoginUserUtil.getLoginUser();

        String encryptPassword = LoginUserUtil.encryptPassword(newPassword);
        String username = loginUser.getUsername();
        String password = userMapper.selectPwdByUserName(username);
        if (!LoginUserUtil.matchesPassword(oldPassword, password)) {
            return fail("修改密码失败，旧密码错误");
        }
        if (LoginUserUtil.matchesPassword(newPassword, password)) {
            return fail("新密码不能与旧密码相同");
        }
        SysUser update = new SysUser(loginUser.getUserId());
        update.setPassword(encryptPassword);
        return toRes(userMapper.updateById(update));
    }

    @Operation(summary = "更新头像")
    @OperateLog(title = "个人中心", businessType = BusinessType.UPDATE)
    @PutMapping("/avatar")
    public Result<?> updateAvatar(@RequestBody String url) {
        LoginUser loginUser = LoginUserUtil.getLoginUser();
        return toRes(userMapper.updateAvatar(loginUser.getUsername(), url) > 0);
    }
}
