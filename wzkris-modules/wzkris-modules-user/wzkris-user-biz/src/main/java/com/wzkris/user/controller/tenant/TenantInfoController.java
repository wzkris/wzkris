package com.wzkris.user.controller.tenant;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateType;
import com.wzkris.common.orm.annotation.IgnoreTenant;
import com.wzkris.common.orm.model.BaseController;
import com.wzkris.common.security.annotation.CheckUserPerms;
import com.wzkris.common.security.utils.LoginUserUtil;
import com.wzkris.common.web.utils.BeanUtil;
import com.wzkris.user.domain.TenantInfoDO;
import com.wzkris.user.domain.req.EditPwdReq;
import com.wzkris.user.domain.req.tenant.TenantInfoReq;
import com.wzkris.user.domain.vo.tenant.TenantInfoVO;
import com.wzkris.user.domain.vo.tenant.TenantUsedQuotaVO;
import com.wzkris.user.mapper.DeptInfoMapper;
import com.wzkris.user.mapper.RoleInfoMapper;
import com.wzkris.user.mapper.TenantInfoMapper;
import com.wzkris.user.mapper.UserInfoMapper;
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
@CheckUserPerms("user-mod:tenant-info")
@RequestMapping("/tenant-info")
@IgnoreTenant(value = false, forceTenantId = "@lu.getTenantId()") // 忽略切换
@RequiredArgsConstructor
public class TenantInfoController extends BaseController {

    private final UserInfoMapper userInfoMapper;

    private final RoleInfoMapper roleInfoMapper;

    private final DeptInfoMapper deptInfoMapper;

    private final TenantInfoMapper tenantInfoMapper;

    private final PasswordEncoder passwordEncoder;

    @Operation(summary = "获取信息")
    @GetMapping
    public Result<TenantInfoVO> tenantInfo() {
        return ok(tenantInfoMapper.selectVOById(LoginUserUtil.getTenantId()));
    }

    @Operation(summary = "修改信息")
    @PostMapping
    @CheckUserPerms("user-mod:tenant-info:edit")
    public Result<TenantInfoVO> tenantInfo(@RequestBody TenantInfoReq req) {
        TenantInfoDO sysTenant = BeanUtil.convert(req, new TenantInfoDO(LoginUserUtil.getTenantId()));
        return toRes(tenantInfoMapper.updateById(sysTenant));
    }

    @Operation(summary = "获取已使用配额")
    @GetMapping("/used-quota")
    public Result<TenantUsedQuotaVO> limitInfo() {
        TenantUsedQuotaVO usedQuotaVO = new TenantUsedQuotaVO();
        usedQuotaVO.setAccountHas(Math.toIntExact(userInfoMapper.selectCount(null)));
        usedQuotaVO.setRoleHas(Math.toIntExact(roleInfoMapper.selectCount(null)));
        usedQuotaVO.setDeptHas(Math.toIntExact(deptInfoMapper.selectCount(null)));
        return ok(usedQuotaVO);
    }

    @Operation(summary = "修改操作密码")
    @OperateLog(title = "商户信息", subTitle = "修改操作密码", operateType = OperateType.UPDATE)
    @PostMapping("/edit-operpwd")
    @PreAuthorize("@lu.isAdmin()") // 只允许租户的超级管理员修改
    public Result<Void> editOperPwd(@RequestBody @Validated(EditPwdReq.OperPwd.class) EditPwdReq req) {
        Long tenantId = LoginUserUtil.getTenantId();

        String operPwd = tenantInfoMapper.selectOperPwdById(tenantId);

        if (!passwordEncoder.matches(req.getOldPassword(), operPwd)) {
            return err40000("修改密码失败，旧密码错误");
        }

        if (passwordEncoder.matches(req.getNewPassword(), operPwd)) {
            return err40000("新密码不能与旧密码相同");
        }

        TenantInfoDO update = new TenantInfoDO(tenantId);
        update.setOperPwd(passwordEncoder.encode(req.getNewPassword()));
        return toRes(tenantInfoMapper.updateById(update));
    }

}
