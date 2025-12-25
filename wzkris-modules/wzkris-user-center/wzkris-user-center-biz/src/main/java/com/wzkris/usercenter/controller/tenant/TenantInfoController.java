package com.wzkris.usercenter.controller.tenant;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateTypeEnum;
import com.wzkris.common.orm.model.BaseController;
import com.wzkris.common.security.annotation.CheckTenantPerms;
import com.wzkris.common.security.utils.TenantUtil;
import com.wzkris.common.web.utils.BeanUtil;
import com.wzkris.usercenter.domain.TenantInfoDO;
import com.wzkris.usercenter.domain.req.EditPwdReq;
import com.wzkris.usercenter.domain.req.tenant.TenantInfoReq;
import com.wzkris.usercenter.domain.vo.tenant.TenantInfoVO;
import com.wzkris.usercenter.domain.vo.tenant.TenantUsedQuotaVO;
import com.wzkris.usercenter.mapper.AdminInfoMapper;
import com.wzkris.usercenter.mapper.PostInfoMapper;
import com.wzkris.usercenter.mapper.TenantInfoMapper;
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
@CheckTenantPerms("user-mod:tenant-info")
@RequestMapping("/tenant-info")
@RequiredArgsConstructor
public class TenantInfoController extends BaseController {

    private final AdminInfoMapper adminInfoMapper;

    private final PostInfoMapper postInfoMapper;

    private final TenantInfoMapper tenantInfoMapper;

    private final PasswordEncoder passwordEncoder;

    @Operation(summary = "获取信息")
    @GetMapping
    public Result<TenantInfoVO> tenantInfo() {
        return ok(tenantInfoMapper.selectVOById(TenantUtil.getTenantId()));
    }

    @Operation(summary = "修改信息")
    @PostMapping
    @CheckTenantPerms("user-mod:tenant-info:edit")
    public Result<Void> tenantInfo(@RequestBody TenantInfoReq req) {
        TenantInfoDO tenantInfoDO = BeanUtil.convert(req, new TenantInfoDO(TenantUtil.getTenantId()));
        return toRes(tenantInfoMapper.updateById(tenantInfoDO));
    }

    @Operation(summary = "获取已使用配额")
    @GetMapping("/used-quota")
    public Result<TenantUsedQuotaVO> limitInfo() {
        TenantUsedQuotaVO usedQuotaVO = new TenantUsedQuotaVO();
        usedQuotaVO.setAccountHas(Math.toIntExact(adminInfoMapper.selectCount(null)));
        usedQuotaVO.setPostHas(Math.toIntExact(postInfoMapper.selectCount(null)));
        return ok(usedQuotaVO);
    }

    @Operation(summary = "修改操作密码")
    @OperateLog(title = "商户信息", subTitle = "修改操作密码", type = OperateTypeEnum.UPDATE)
    @PostMapping("/edit-operpwd")
    @PreAuthorize("@tu.isAdmin()") // 只允许租户的超级管理员修改
    public Result<Void> editOperPwd(@RequestBody @Validated(EditPwdReq.OperPwd.class) EditPwdReq req) {
        Long tenantId = TenantUtil.getTenantId();

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
