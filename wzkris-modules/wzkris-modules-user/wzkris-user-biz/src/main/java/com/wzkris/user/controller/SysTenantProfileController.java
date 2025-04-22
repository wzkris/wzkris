package com.wzkris.user.controller;

import com.wzkris.common.core.domain.Result;
import com.wzkris.common.core.utils.BeanUtil;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateType;
import com.wzkris.common.orm.annotation.IgnoreTenant;
import com.wzkris.common.security.oauth2.annotation.CheckSystemPerms;
import com.wzkris.common.security.utils.LoginUtil;
import com.wzkris.common.web.model.BaseController;
import com.wzkris.user.domain.SysTenant;
import com.wzkris.user.domain.req.EditPwdReq;
import com.wzkris.user.domain.req.SysTenantProfileReq;
import com.wzkris.user.domain.vo.SysTenantProfileVO;
import com.wzkris.user.domain.vo.SysTenantUsedQuotaVO;
import com.wzkris.user.mapper.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RestController
@CheckSystemPerms("tenant:info")
@RequestMapping("/tenant_profile")
@IgnoreTenant(value = false, forceTenantId = "@lg.getTenantId()")// 忽略切换
@RequiredArgsConstructor
public class SysTenantProfileController extends BaseController {

    private final SysUserMapper userMapper;

    private final SysRoleMapper roleMapper;

    private final SysPostMapper postMapper;

    private final SysDeptMapper deptMapper;

    private final SysTenantMapper tenantMapper;

    private final PasswordEncoder passwordEncoder;

    @Operation(summary = "获取信息")
    @GetMapping
    public Result<SysTenantProfileVO> tenantInfo() {
        return ok(tenantMapper.selectVOById(LoginUtil.getTenantId()));
    }

    @Operation(summary = "修改信息")
    @PostMapping
    public Result<SysTenantProfileVO> tenantInfo(@RequestBody SysTenantProfileReq req) {
        SysTenant sysTenant = BeanUtil.convert(req, new SysTenant(LoginUtil.getTenantId()));
        return toRes(tenantMapper.updateById(sysTenant));
    }

    @Operation(summary = "获取已使用配额")
    @GetMapping("/used_quota")
    public Result<SysTenantUsedQuotaVO> limitInfo() {
        SysTenantUsedQuotaVO usedQuotaVO = new SysTenantUsedQuotaVO();
        usedQuotaVO.setAccountHas(Math.toIntExact(userMapper.selectCount(null)));
        usedQuotaVO.setRoleHas(Math.toIntExact(roleMapper.selectCount(null)));
        usedQuotaVO.setPostHas(Math.toIntExact(postMapper.selectCount(null)));
        usedQuotaVO.setDeptHas(Math.toIntExact(deptMapper.selectCount(null)));
        return ok(usedQuotaVO);
    }

    @Operation(summary = "修改操作密码")
    @OperateLog(title = "商户信息", subTitle = "修改操作密码", operateType = OperateType.UPDATE)
    @PostMapping("/edit_operpwd")
    @PreAuthorize("@lg.isAdmin()")// 只允许租户的超级管理员修改
    public Result<Void> editOperPwd(@RequestBody @Validated(EditPwdReq.OperPwd.class) EditPwdReq req) {
        Long tenantId = LoginUtil.getTenantId();

        String operPwd = tenantMapper.selectOperPwdById(tenantId);

        if (!passwordEncoder.matches(req.getOldPassword(), operPwd)) {
            return err412("修改密码失败，旧密码错误");
        }

        if (passwordEncoder.matches(req.getNewPassword(), operPwd)) {
            return err412("新密码不能与旧密码相同");
        }

        SysTenant update = new SysTenant(tenantId);
        update.setOperPwd(passwordEncoder.encode(req.getNewPassword()));
        return toRes(tenantMapper.updateById(update));
    }

}
