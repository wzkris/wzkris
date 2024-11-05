package com.wzkris.user.controller;

import cn.hutool.core.util.NumberUtil;
import com.wzkris.common.core.domain.Result;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.orm.model.BaseController;
import com.wzkris.common.security.utils.SysUtil;
import com.wzkris.user.domain.SysTenant;
import com.wzkris.user.domain.dto.EditPasswordDTO;
import com.wzkris.user.domain.vo.SysTenantVO;
import com.wzkris.user.mapper.SysTenantMapper;
import com.wzkris.user.mapper.SysUserMapper;
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
@RequiredArgsConstructor
@RestController
@RequestMapping("/tenant")
public class SysTenantOwnController extends BaseController {
    private final PasswordEncoder passwordEncoder;
    private final SysUserMapper userMapper;
    private final SysTenantMapper tenantMapper;

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
    @PostMapping("/edit_operpwd")
    @PreAuthorize("@SysUtil.isAdministrator()")// 只允许租户的超级管理员修改
    public Result<?> editOperPwd(@RequestBody EditPasswordDTO dto) {
        if (StringUtil.length(dto.getOldPassword()) != 6 || StringUtil.length(dto.getNewPassword()) != 6) {
            return fail("密码长度不正确");
        }
        if (!NumberUtil.isNumber(dto.getOldPassword()) || !NumberUtil.isNumber(dto.getNewPassword())) {
            return fail("密码不正确");
        }
        SysTenant sysTenant = tenantMapper.selectById(SysUtil.getTenantId());
        if (!passwordEncoder.matches(dto.getOldPassword(), sysTenant.getOperPwd())) {
            return fail("密码错误");
        }
        SysTenant update = new SysTenant(sysTenant.getTenantId());
        update.setOperPwd(passwordEncoder.encode(dto.getNewPassword()));
        return toRes(tenantMapper.updateById(update));
    }

}
