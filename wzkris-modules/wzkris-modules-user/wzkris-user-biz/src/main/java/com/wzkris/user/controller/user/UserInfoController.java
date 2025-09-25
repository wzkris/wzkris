package com.wzkris.user.controller.user;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wzkris.auth.feign.captcha.CaptchaFeign;
import com.wzkris.auth.feign.captcha.req.CaptchaCheckReq;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateType;
import com.wzkris.common.orm.annotation.IgnoreTenant;
import com.wzkris.common.orm.model.BaseController;
import com.wzkris.common.redis.annotation.GlobalCache;
import com.wzkris.common.redis.annotation.GlobalCacheEvict;
import com.wzkris.common.security.utils.LoginUserUtil;
import com.wzkris.user.domain.UserInfoDO;
import com.wzkris.user.domain.req.EditPhoneReq;
import com.wzkris.user.domain.req.EditPwdReq;
import com.wzkris.user.domain.req.user.UserInfoReq;
import com.wzkris.user.domain.vo.userinfo.ChatPersonVO;
import com.wzkris.user.domain.vo.userinfo.UserInfoVO;
import com.wzkris.user.mapper.DeptInfoMapper;
import com.wzkris.user.mapper.UserInfoMapper;
import com.wzkris.user.service.RoleInfoService;
import com.wzkris.user.service.UserInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
@Tag(name = "用户信息")
@RestController
@RequestMapping("/user-info")
@IgnoreTenant(value = false, forceTenantId = "@lu.getTenantId()") // 忽略切换
@RequiredArgsConstructor
public class UserInfoController extends BaseController {

    private final String info_prefix = "userinfo";

    private final UserInfoMapper userInfoMapper;

    private final UserInfoService userInfoService;

    private final RoleInfoService roleInfoService;

    private final DeptInfoMapper deptInfoMapper;

    private final CaptchaFeign captchaFeign;

    private final PasswordEncoder passwordEncoder;

    @Operation(summary = "账户信息")
    @GetMapping
    @GlobalCache(keyPrefix = info_prefix, key = "@lu.getId()", ttl = 600_000, sync = true) // TODO 这里缓存的需要在退出时移除
    public Result<UserInfoVO> userinfo() {
        UserInfoDO user = userInfoMapper.selectById(LoginUserUtil.getId());

        if (user == null) {// 降级会走到这
            user = new UserInfoDO();
        }
        UserInfoVO userInfoVO = new UserInfoVO();
        userInfoVO.setAdmin(LoginUserUtil.isAdmin());
        userInfoVO.setSuperTenant(LoginUserUtil.isSuperTenant());
        userInfoVO.setUsername(LoginUserUtil.getUsername());
        userInfoVO.setAuthorities(LoginUserUtil.getAuthorities());
        userInfoVO.setAvatar(user.getAvatar());
        userInfoVO.setNickname(user.getNickname());
        userInfoVO.setEmail(user.getEmail());
        userInfoVO.setPhoneNumber(user.getPhoneNumber());
        userInfoVO.setGender(user.getGender());
        userInfoVO.setLoginDate(user.getLoginDate());

        userInfoVO.setDeptName(deptInfoMapper.selectDeptNameById(user.getDeptId()));
        userInfoVO.setRoleGroup(roleInfoService.getRoleGroup());
        return ok(userInfoVO);
    }

    @Operation(summary = "聊天人员列表")
    @GetMapping("/chat-person-list")
    public Result<List<ChatPersonVO>> chatPersonList() {
        List<UserInfoDO> userInfoDOS = userInfoMapper.selectList(Wrappers.lambdaQuery(UserInfoDO.class)
                .select(UserInfoDO::getUserId, UserInfoDO::getNickname, UserInfoDO::getAvatar)
                .ne(UserInfoDO::getUserId, LoginUserUtil.getId()));

        return ok(cast2ChatVO(userInfoDOS));
    }

    private List<ChatPersonVO> cast2ChatVO(List<UserInfoDO> userInfoDOS) {
        return userInfoDOS.stream().map(userInfoDO ->
                        new ChatPersonVO(userInfoDO.getUserId(), userInfoDO.getNickname(), userInfoDO.getAvatar()))
                .collect(Collectors.toList());
    }

    @Operation(summary = "修改基本信息")
    @OperateLog(title = "个人信息", subTitle = "修改基本信息", operateType = OperateType.UPDATE)
    @PostMapping
    @GlobalCacheEvict(keyPrefix = info_prefix, key = "@lu.getId()")
    public Result<Void> editInfo(@RequestBody UserInfoReq profileReq) {
        UserInfoDO user = new UserInfoDO(LoginUserUtil.getId());
        user.setNickname(profileReq.getNickname());
        user.setGender(profileReq.getGender());
        return toRes(userInfoMapper.updateById(user));
    }

    @Operation(summary = "修改手机号")
    @OperateLog(title = "个人信息", subTitle = "修改手机号", operateType = OperateType.UPDATE)
    @PostMapping("/edit-phonenumber")
    @GlobalCacheEvict(keyPrefix = info_prefix, key = "@lu.getId()")
    public Result<Void> editPhoneNumber(@RequestBody @Valid EditPhoneReq req) {
        Long userId = LoginUserUtil.getId();

        if (userInfoService.existByPhoneNumber(userId, req.getPhoneNumber())) {
            return err40000("该手机号已被使用");
        }
        // 验证
        CaptchaCheckReq captchaCheckReq = new CaptchaCheckReq(userInfoMapper.selectPhoneNumberById(userId), req.getSmsCode());
        if (!captchaFeign.validateCaptcha(captchaCheckReq)) {
            return err40000("验证码错误");
        }

        UserInfoDO user = new UserInfoDO(userId);
        user.setPhoneNumber(req.getPhoneNumber());
        return toRes(userInfoMapper.updateById(user));
    }

    @Operation(summary = "修改密码")
    @OperateLog(title = "个人信息", subTitle = "修改密码", operateType = OperateType.UPDATE)
    @PostMapping("/edit-password")
    public Result<Void> editPwd(@RequestBody @Validated(EditPwdReq.LoginPwd.class) EditPwdReq req) {
        Long userId = LoginUserUtil.getId();

        String password = userInfoMapper.selectPwdById(userId);

        if (!passwordEncoder.matches(req.getOldPassword(), password)) {
            return err40000("修改密码失败，旧密码错误");
        }

        if (passwordEncoder.matches(req.getNewPassword(), password)) {
            return err40000("新密码不能与旧密码相同");
        }

        UserInfoDO update = new UserInfoDO(userId);
        update.setPassword(passwordEncoder.encode(req.getNewPassword()));
        return toRes(userInfoMapper.updateById(update));
    }

    @Operation(summary = "更新头像")
    @OperateLog(title = "个人信息", subTitle = "更新头像", operateType = OperateType.UPDATE)
    @PostMapping("/edit-avatar")
    @GlobalCacheEvict(keyPrefix = info_prefix, key = "@lu.getId()")
    public Result<Void> editAvatar(@RequestBody String url) {
        UserInfoDO userInfoDO = new UserInfoDO(LoginUserUtil.getId());
        userInfoDO.setAvatar(url);
        return toRes(userInfoMapper.updateById(userInfoDO));
    }

}
