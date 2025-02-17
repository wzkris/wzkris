package com.wzkris.user.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.core.domain.Result;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateType;
import com.wzkris.common.orm.page.Page;
import com.wzkris.common.security.oauth2.annotation.CheckPerms;
import com.wzkris.common.security.utils.LoginUserUtil;
import com.wzkris.common.web.model.BaseController;
import com.wzkris.user.domain.SysTenant;
import com.wzkris.user.domain.SysTenantWalletRecord;
import com.wzkris.user.domain.req.EditPwdReq;
import com.wzkris.user.domain.req.SysTenantWalletRecordQueryReq;
import com.wzkris.user.domain.req.WithdrawalReq;
import com.wzkris.user.domain.vo.SysTenantLimitVO;
import com.wzkris.user.domain.vo.SysTenantOwnVO;
import com.wzkris.user.domain.vo.SysTenantWalletVO;
import com.wzkris.user.mapper.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 自身租户信息
 *
 * @author wzkris
 */
@Tag(name = "商户信息")
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/tenant")
public class SysTenantOwnController extends BaseController {

    private final SysUserMapper userMapper;

    private final SysRoleMapper roleMapper;

    private final SysPostMapper postMapper;

    private final SysDeptMapper deptMapper;

    private final SysTenantMapper tenantMapper;

    private final SysTenantWalletMapper tenantWalletMapper;

    private final SysTenantWalletRecordMapper tenantWalletRecordMapper;

    private final PasswordEncoder passwordEncoder;

    @Operation(summary = "获取自身租户")
    @GetMapping("/info")
    @CheckPerms("tenant:information")
    public Result<SysTenantOwnVO> tenantInfo() {
        Long tenantId = LoginUserUtil.getTenantId();
        SysTenantOwnVO tenantVO = tenantMapper.selectVOById(tenantId);
        return ok(tenantVO);
    }

    @Operation(summary = "获取租户关联信息")
    @GetMapping("/limit_info")
    @CheckPerms("tenant:information")
    public Result<SysTenantLimitVO> limitInfo() {
        SysTenantLimitVO limitVO = new SysTenantLimitVO();
        limitVO.setAccountHas(Math.toIntExact(userMapper.selectCount(null)));
        limitVO.setRoleHas(Math.toIntExact(roleMapper.selectCount(null)));
        limitVO.setPostHas(Math.toIntExact(postMapper.selectCount(null)));
        limitVO.setDeptHas(Math.toIntExact(deptMapper.selectCount(null)));
        return ok(limitVO);
    }

    @Operation(summary = "获取余额信息")
    @GetMapping("/wallet_info")
    @CheckPerms("tenant:wallet_info")
    public Result<SysTenantWalletVO> walletInfo() {
        return ok(tenantWalletMapper.selectById2VO(LoginUserUtil.getTenantId(), SysTenantWalletVO.class));
    }

    @Operation(summary = "获取租户钱包记录")
    @GetMapping("/wallet_record/list")
    @CheckPerms("wallet_record:list")
    public Result<Page<SysTenantWalletRecord>> listWalletPage(SysTenantWalletRecordQueryReq queryReq) {
        startPage();
        List<SysTenantWalletRecord> recordList = tenantWalletRecordMapper.selectList(this.buildWalletQueryWrapper(queryReq));
        return getDataTable(recordList);
    }

    private LambdaQueryWrapper<SysTenantWalletRecord> buildWalletQueryWrapper(SysTenantWalletRecordQueryReq queryReq) {
        return new LambdaQueryWrapper<SysTenantWalletRecord>()
                .like(StringUtil.isNotBlank(queryReq.getType()), SysTenantWalletRecord::getType, queryReq.getType())
                .between(queryReq.getParams().get("beginTime") != null && queryReq.getParams().get("endTime") != null,
                        SysTenantWalletRecord::getCreateAt, queryReq.getParams().get("beginTime"), queryReq.getParams().get("endTime"))
                .orderByDesc(SysTenantWalletRecord::getRecordId);
    }

    @Operation(summary = "修改操作密码")
    @OperateLog(title = "商户信息", subTitle = "修改操作密码", operateType = OperateType.UPDATE)
    @PostMapping("/edit_operpwd")
    @PreAuthorize("@lg.isAdmin()")// 只允许租户的超级管理员修改
    public Result<Void> editOperPwd(@RequestBody @Validated(EditPwdReq.OperPwd.class) EditPwdReq req) {
        Long tenantId = LoginUserUtil.getTenantId();

        String operPwd = tenantMapper.selectOperPwdById(tenantId);

        if (!passwordEncoder.matches(req.getOldPassword(), operPwd)) {
            return error412("修改密码失败，旧密码错误");
        }

        if (passwordEncoder.matches(req.getNewPassword(), operPwd)) {
            return error412("新密码不能与旧密码相同");
        }

        SysTenant update = new SysTenant(tenantId);
        update.setOperPwd(passwordEncoder.encode(req.getNewPassword()));
        return toRes(tenantMapper.updateById(update));
    }

    @Operation(summary = "提现")
    @OperateLog(title = "商户信息", subTitle = "提现", operateType = OperateType.OTHER)
    @PostMapping("/wallet/withdrawal")
    @CheckPerms("tenant:withdrawal")
    public Result<Void> withdrawal(@RequestBody @Valid WithdrawalReq req) {
        SysTenant sysTenant = tenantMapper.selectById(LoginUserUtil.getTenantId());
        if (!passwordEncoder.matches(req.getOperPwd(), sysTenant.getOperPwd())) {
            return error412("密码错误");
        }
        // TODO 实际提现
        return ok();
    }

}
