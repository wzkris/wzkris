package com.wzkris.user.controller;

import cn.hutool.core.util.NumberUtil;
import com.wzkris.common.core.domain.Result;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateType;
import com.wzkris.common.security.utils.SysUtil;
import com.wzkris.common.web.model.BaseController;
import com.wzkris.user.domain.SysTenant;
import com.wzkris.user.domain.req.EditPwdReq;
import com.wzkris.user.domain.req.WithdrawalReq;
import com.wzkris.user.domain.vo.SysTenantVO;
import com.wzkris.user.mapper.SysTenantMapper;
import com.wzkris.user.mapper.SysUserMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 自身租户信息
 *
 * @author wzkris
 */
@Tag(name = "租户信息")
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/tenant")
public class SysTenantOwnController extends BaseController {
    private final SysUserMapper userMapper;
    private final SysTenantMapper tenantMapper;
    private final PasswordEncoder passwordEncoder;

    @Operation(summary = "获取自身租户")
    @GetMapping("/getinfo")
    @PreAuthorize("@ps.hasPerms('tenant:getinfo')")
    public Result<SysTenantVO> getInfo() {
        Long tenantId = SysUtil.getTenantId();
        SysTenantVO tenantVO;
        if (SysTenant.isSuperTenant(tenantId)) {
            tenantVO = new SysTenantVO(true);// 超级租户不存在信息
        }
        else {
            tenantVO = tenantMapper.selectVOById(tenantId);
            // 查询已有账号
            Long count = userMapper.selectCount(null);
            tenantVO.setAccountHas(Math.toIntExact(count));
        }
        return ok(tenantVO);
    }

    @Operation(summary = "修改操作密码")
    @OperateLog(title = "租户信息", subTitle = "修改操作密码", operateType = OperateType.UPDATE)
    @PostMapping("/edit_operpwd")
    @PreAuthorize("@SysUtil.isAdministrator()")// 只允许租户的超级管理员修改
    public Result<Void> editOperPwd(@RequestBody @Valid EditPwdReq req) {
        if (StringUtil.length(req.getOldPassword()) != 6 || StringUtil.length(req.getNewPassword()) != 6) {
            return fail("密码长度不正确");
        }
        if (!NumberUtil.isNumber(req.getOldPassword()) || !NumberUtil.isNumber(req.getNewPassword())) {
            return fail("密码不正确");
        }
        SysTenant sysTenant = tenantMapper.selectById(SysUtil.getTenantId());
        if (!passwordEncoder.matches(req.getOldPassword(), sysTenant.getOperPwd())) {
            return fail("密码错误");
        }
        SysTenant update = new SysTenant(sysTenant.getTenantId());
        update.setOperPwd(passwordEncoder.encode(req.getNewPassword()));
        return toRes(tenantMapper.updateById(update));
    }

    @Operation(summary = "提现")
    @OperateLog(title = "租户信息", subTitle = "提现", operateType = OperateType.OTHER)
    @PostMapping("/wallet/withdrawal")
    public Result<Void> withdrawal(@RequestBody @Valid WithdrawalReq req) {
        SysTenant sysTenant = tenantMapper.selectById(SysUtil.getTenantId());
        if (!passwordEncoder.matches(req.getOperPwd(), sysTenant.getOperPwd())) {
            return fail("密码错误");
        }
        // TODO 实际提现
        return ok();
    }

}
