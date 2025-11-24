package com.wzkris.principal.controller.admin;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wzkris.auth.httpservice.captcha.CaptchaHttpService;
import com.wzkris.auth.httpservice.captcha.req.CaptchaCheckReq;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateType;
import com.wzkris.common.orm.model.BaseController;
import com.wzkris.common.redis.annotation.GlobalCache;
import com.wzkris.common.redis.annotation.GlobalCacheEvict;
import com.wzkris.common.security.utils.AdminUtil;
import com.wzkris.principal.domain.AdminInfoDO;
import com.wzkris.principal.domain.req.EditPhoneReq;
import com.wzkris.principal.domain.req.EditPwdReq;
import com.wzkris.principal.domain.req.admin.AdminInfoReq;
import com.wzkris.principal.domain.vo.admin.AdminInfoVO;
import com.wzkris.principal.domain.vo.admin.ChatPersonVO;
import com.wzkris.principal.mapper.AdminInfoMapper;
import com.wzkris.principal.mapper.DeptInfoMapper;
import com.wzkris.principal.service.AdminInfoService;
import com.wzkris.principal.service.RoleInfoService;
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

    private final CaptchaHttpService captchaHttpService;

    private final PasswordEncoder passwordEncoder;

    @Operation(summary = "账户信息")
    @GetMapping
    @GlobalCache(keyPrefix = info_prefix, key = "@au.getId()", ttl = 600_000, sync = true) // TODO 这里缓存的需要在退出时移除
    public Result<AdminInfoVO> userinfo() {
        AdminInfoDO adminInfoDO = adminInfoMapper.selectById(AdminUtil.getId());

        if (adminInfoDO == null) {// 降级会走到这
            adminInfoDO = new AdminInfoDO();
        }
        AdminInfoVO adminInfoVO = new AdminInfoVO();
        adminInfoVO.setAdmin(AdminUtil.isAdmin());
        adminInfoVO.setUsername(AdminUtil.getUsername());
        adminInfoVO.setAuthorities(AdminUtil.getAuthorities());
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
    public Result<List<ChatPersonVO>> chatPersonList() {
        List<AdminInfoDO> adminInfoDOS = adminInfoMapper.selectList(Wrappers.lambdaQuery(AdminInfoDO.class)
                .select(AdminInfoDO::getAdminId, AdminInfoDO::getNickname, AdminInfoDO::getAvatar)
                .ne(AdminInfoDO::getAdminId, AdminUtil.getId()));

        return ok(cast2ChatVO(adminInfoDOS));
    }

    private List<ChatPersonVO> cast2ChatVO(List<AdminInfoDO> adminInfoDOS) {
        return adminInfoDOS.stream().map(userInfoDO ->
                        new ChatPersonVO(userInfoDO.getAdminId(), userInfoDO.getNickname(), userInfoDO.getAvatar()))
                .collect(Collectors.toList());
    }

    @Operation(summary = "修改基本信息")
    @OperateLog(title = "个人信息", subTitle = "修改基本信息", operateType = OperateType.UPDATE)
    @PostMapping
    @GlobalCacheEvict(keyPrefix = info_prefix, key = "@au.getId()")
    public Result<Void> editInfo(@RequestBody AdminInfoReq profileReq) {
        AdminInfoDO admin = new AdminInfoDO(AdminUtil.getId());
        admin.setNickname(profileReq.getNickname());
        admin.setGender(profileReq.getGender());
        return toRes(adminInfoMapper.updateById(admin));
    }

    @Operation(summary = "修改手机号")
    @OperateLog(title = "个人信息", subTitle = "修改手机号", operateType = OperateType.UPDATE)
    @PostMapping("/edit-phonenumber")
    @GlobalCacheEvict(keyPrefix = info_prefix, key = "@au.getId()")
    public Result<Void> editPhoneNumber(@RequestBody @Valid EditPhoneReq req) {
        Long adminId = AdminUtil.getId();

        if (adminInfoService.existByPhoneNumber(adminId, req.getPhoneNumber())) {
            return err40000("该手机号已被使用");
        }
        // 验证
        CaptchaCheckReq captchaCheckReq = new CaptchaCheckReq(adminInfoMapper.selectPhoneNumberById(adminId), req.getSmsCode());
        if (!captchaHttpService.validateCaptcha(captchaCheckReq)) {
            return err40000("验证码错误");
        }

        AdminInfoDO admin = new AdminInfoDO(adminId);
        admin.setPhoneNumber(req.getPhoneNumber());
        return toRes(adminInfoMapper.updateById(admin));
    }

    @Operation(summary = "修改密码")
    @OperateLog(title = "个人信息", subTitle = "修改密码", operateType = OperateType.UPDATE)
    @PostMapping("/edit-password")
    public Result<Void> editPwd(@RequestBody @Validated(EditPwdReq.LoginPwd.class) EditPwdReq req) {
        Long adminId = AdminUtil.getId();

        String password = adminInfoMapper.selectPwdById(adminId);

        if (!passwordEncoder.matches(req.getOldPassword(), password)) {
            return err40000("修改密码失败，旧密码错误");
        }

        if (passwordEncoder.matches(req.getNewPassword(), password)) {
            return err40000("新密码不能与旧密码相同");
        }

        AdminInfoDO update = new AdminInfoDO(adminId);
        update.setPassword(passwordEncoder.encode(req.getNewPassword()));
        return toRes(adminInfoMapper.updateById(update));
    }

    @Operation(summary = "更新头像")
    @OperateLog(title = "个人信息", subTitle = "更新头像", operateType = OperateType.UPDATE)
    @PostMapping("/edit-avatar")
    @GlobalCacheEvict(keyPrefix = info_prefix, key = "@au.getId()")
    public Result<Void> editAvatar(@RequestBody String url) {
        AdminInfoDO adminInfoDO = new AdminInfoDO(AdminUtil.getId());
        adminInfoDO.setAvatar(url);
        return toRes(adminInfoMapper.updateById(adminInfoDO));
    }

}
