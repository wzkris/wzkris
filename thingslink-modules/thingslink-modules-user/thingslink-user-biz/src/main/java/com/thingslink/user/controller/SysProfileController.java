package com.thingslink.user.controller;

import cn.hutool.core.map.MapUtil;
import com.thingslink.auth.api.RemoteCaptchaApi;
import com.thingslink.auth.api.domain.SmsDTO;
import com.thingslink.common.core.domain.Result;
import com.thingslink.common.core.utils.StringUtil;
import com.thingslink.common.log.annotation.OperateLog;
import com.thingslink.common.log.enums.OperateType;
import com.thingslink.common.orm.model.BaseController;
import com.thingslink.common.security.oauth2.model.LoginSysUser;
import com.thingslink.common.security.utils.SysUtil;
import com.thingslink.user.domain.SysUser;
import com.thingslink.user.domain.dto.SysUserDTO;
import com.thingslink.user.mapper.SysDeptMapper;
import com.thingslink.user.mapper.SysUserMapper;
import com.thingslink.user.service.SysPostService;
import com.thingslink.user.service.SysRoleService;
import com.thingslink.user.service.SysUserService;
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
    @PutMapping
    public Result<?> update(@RequestBody SysUserDTO sysUserDTO) {
        SysUser user = new SysUser(SysUtil.getUserId());
        user.setNickname(sysUserDTO.getNickname());
        user.setGender(sysUserDTO.getGender());
        return toRes(userMapper.updateById(user));
    }

    @Operation(summary = "修改手机号")
    @OperateLog(title = "个人中心", operateType = OperateType.UPDATE)
    @PutMapping("/phonenumber")
    public Result<?> updatePhoneNumber(String phoneNumber, String smsCode) {
        Long userId = SysUtil.getUserId();

        if (userService.checkUserUnique(new SysUser().setPhoneNumber(phoneNumber), userId)) {
            return fail("该手机号已被使用");
        }
        // 验证
        SmsDTO smsDTO = new SmsDTO(userMapper.selectPhoneNumberById(userId), smsCode);
        Result<Void> result = remoteCaptchaApi.validateSms(smsDTO);
        result.checkData();

        SysUser user = new SysUser(userId);
        user.setPhoneNumber(phoneNumber);
        return toRes(userMapper.updateById(user));
    }

    @Operation(summary = "修改密码")
    @OperateLog(title = "个人中心", operateType = OperateType.UPDATE)
    @PutMapping("/password")
    public Result<?> updatePwd(String oldPassword, String newPassword) {
        LoginSysUser loginUser = SysUtil.getLoginUser();

        String encryptPassword = SysUtil.encryptPassword(newPassword);
        String username = loginUser.getUsername();
        String password = userMapper.selectPwdByUserName(username);
        if (!SysUtil.matchesPassword(oldPassword, password)) {
            return fail("修改密码失败，旧密码错误");
        }
        if (SysUtil.matchesPassword(newPassword, password)) {
            return fail("新密码不能与旧密码相同");
        }
        SysUser update = new SysUser(loginUser.getUserId());
        update.setPassword(encryptPassword);
        return toRes(userMapper.updateById(update));
    }

    @Operation(summary = "更新头像")
    @OperateLog(title = "个人中心", operateType = OperateType.UPDATE)
    @PutMapping("/avatar")
    public Result<?> updateAvatar(@RequestBody String url) {
        LoginSysUser loginUser = SysUtil.getLoginUser();
        return toRes(userMapper.updateAvatar(loginUser.getUsername(), url) > 0);
    }
}
