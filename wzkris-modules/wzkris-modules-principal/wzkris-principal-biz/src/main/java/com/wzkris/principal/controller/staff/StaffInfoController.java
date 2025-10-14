package com.wzkris.principal.controller.staff;

import com.wzkris.auth.feign.captcha.CaptchaFeign;
import com.wzkris.auth.feign.captcha.req.CaptchaCheckReq;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateType;
import com.wzkris.common.orm.model.BaseController;
import com.wzkris.common.security.utils.LoginStaffUtil;
import com.wzkris.principal.domain.StaffInfoDO;
import com.wzkris.principal.domain.req.EditPhoneReq;
import com.wzkris.principal.domain.req.EditPwdReq;
import com.wzkris.principal.domain.vo.staffinfo.StaffInfoVO;
import com.wzkris.principal.mapper.StaffInfoMapper;
import com.wzkris.principal.service.PostInfoService;
import com.wzkris.principal.service.StaffInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 员工管理
 *
 * @author wzkris
 */
@Tag(name = "租户员工信息")
@Validated
@RestController
@RequestMapping("/staff-info")
@RequiredArgsConstructor
public class StaffInfoController extends BaseController {

    private final StaffInfoMapper staffInfoMapper;

    private final StaffInfoService staffInfoService;

    private final PostInfoService postInfoService;

    private final CaptchaFeign captchaFeign;

    private final PasswordEncoder passwordEncoder;

    @Operation(summary = "账户信息")
    @GetMapping
    public Result<StaffInfoVO> userinfo() {
        StaffInfoDO staff = staffInfoMapper.selectById(LoginStaffUtil.getId());

        if (staff == null) {// 降级会走到这
            staff = new StaffInfoDO();
        }
        StaffInfoVO staffInfoVO = new StaffInfoVO();
        staffInfoVO.setAdmin(LoginStaffUtil.isAdmin());
        staffInfoVO.setStaffName(LoginStaffUtil.getStaffName());
        staffInfoVO.setAuthorities(LoginStaffUtil.getAuthorities());
        staffInfoVO.setAvatar(staff.getAvatar());
        staffInfoVO.setPhoneNumber(staff.getPhoneNumber());
        staffInfoVO.setGender(staff.getGender());
        staffInfoVO.setLoginDate(staff.getLoginDate());

        staffInfoVO.setPostGroup(postInfoService.getPostGroup());
        return ok(staffInfoVO);
    }

    @Operation(summary = "修改手机号")
    @OperateLog(title = "个人信息", subTitle = "修改手机号", operateType = OperateType.UPDATE)
    @PostMapping("/edit-phonenumber")
    public Result<Void> editPhoneNumber(@RequestBody @Valid EditPhoneReq req) {
        Long staffId = LoginStaffUtil.getId();

        if (staffInfoService.existByPhoneNumber(staffId, req.getPhoneNumber())) {
            return err40000("该手机号已被使用");
        }
        // 验证
        CaptchaCheckReq captchaCheckReq = new CaptchaCheckReq(staffInfoMapper.selectPhoneNumberById(staffId), req.getSmsCode());
        if (!captchaFeign.validateCaptcha(captchaCheckReq)) {
            return err40000("验证码错误");
        }

        StaffInfoDO staff = new StaffInfoDO(staffId);
        staff.setPhoneNumber(req.getPhoneNumber());
        return toRes(staffInfoMapper.updateById(staff));
    }

    @Operation(summary = "修改密码")
    @OperateLog(title = "个人信息", subTitle = "修改密码", operateType = OperateType.UPDATE)
    @PostMapping("/edit-password")
    public Result<Void> editPwd(@RequestBody @Validated(EditPwdReq.LoginPwd.class) EditPwdReq req) {
        Long staffId = LoginStaffUtil.getId();

        String password = staffInfoMapper.selectPwdById(staffId);

        if (!passwordEncoder.matches(req.getOldPassword(), password)) {
            return err40000("修改密码失败，旧密码错误");
        }

        if (passwordEncoder.matches(req.getNewPassword(), password)) {
            return err40000("新密码不能与旧密码相同");
        }

        StaffInfoDO update = new StaffInfoDO(staffId);
        update.setPassword(passwordEncoder.encode(req.getNewPassword()));
        return toRes(staffInfoMapper.updateById(update));
    }

    @Operation(summary = "更新头像")
    @OperateLog(title = "个人信息", subTitle = "更新头像", operateType = OperateType.UPDATE)
    @PostMapping("/edit-avatar")
    public Result<Void> editAvatar(@RequestBody String url) {
        StaffInfoDO staffInfoDO = new StaffInfoDO(LoginStaffUtil.getId());
        staffInfoDO.setAvatar(url);
        return toRes(staffInfoMapper.updateById(staffInfoDO));
    }

}
