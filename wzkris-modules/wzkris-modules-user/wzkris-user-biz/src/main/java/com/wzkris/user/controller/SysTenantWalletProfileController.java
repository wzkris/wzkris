package com.wzkris.user.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.core.domain.Result;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateType;
import com.wzkris.common.orm.annotation.IgnoreTenant;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.security.oauth2.annotation.CheckSystemPerms;
import com.wzkris.common.security.utils.SystemUserUtil;
import com.wzkris.common.orm.model.BaseController;
import com.wzkris.user.domain.SysTenant;
import com.wzkris.user.domain.SysTenantWalletRecord;
import com.wzkris.user.domain.req.SysTenantWalletRecordQueryReq;
import com.wzkris.user.domain.req.WithdrawalReq;
import com.wzkris.user.domain.vo.SysTenantWalletVO;
import com.wzkris.user.mapper.SysTenantMapper;
import com.wzkris.user.mapper.SysTenantWalletMapper;
import com.wzkris.user.mapper.SysTenantWalletRecordMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 租户钱包信息
 *
 * @author wzkris
 */
@Tag(name = "租户钱包信息")
@Validated
@RestController
@CheckSystemPerms("tenant:wallet_info")
@RequestMapping("/tenant_wallet")
@IgnoreTenant(value = false, forceTenantId = "@su.getTenantId()") // 忽略切换
@RequiredArgsConstructor
public class SysTenantWalletProfileController extends BaseController {

    private final SysTenantMapper tenantMapper;

    private final SysTenantWalletMapper tenantWalletMapper;

    private final SysTenantWalletRecordMapper tenantWalletRecordMapper;

    private final PasswordEncoder passwordEncoder;

    @Operation(summary = "余额信息")
    @GetMapping
    public Result<SysTenantWalletVO> walletInfo() {
        return ok(tenantWalletMapper.selectById2VO(SystemUserUtil.getTenantId(), SysTenantWalletVO.class));
    }

    @Operation(summary = "钱包记录")
    @GetMapping("/record")
    public Result<Page<SysTenantWalletRecord>> listWalletPage(SysTenantWalletRecordQueryReq queryReq) {
        startPage();
        List<SysTenantWalletRecord> recordList =
                tenantWalletRecordMapper.selectList(this.buildWalletQueryWrapper(queryReq));
        return getDataTable(recordList);
    }

    private LambdaQueryWrapper<SysTenantWalletRecord> buildWalletQueryWrapper(SysTenantWalletRecordQueryReq queryReq) {
        return new LambdaQueryWrapper<SysTenantWalletRecord>()
                .like(
                        StringUtil.isNotBlank(queryReq.getRecordType()),
                        SysTenantWalletRecord::getRecordType,
                        queryReq.getRecordType())
                .between(
                        queryReq.getParam("beginTime") != null && queryReq.getParam("endTime") != null,
                        SysTenantWalletRecord::getCreateAt,
                        queryReq.getParam("beginTime"),
                        queryReq.getParam("endTime"))
                .orderByDesc(SysTenantWalletRecord::getRecordId);
    }

    @Operation(summary = "提现")
    @OperateLog(title = "商户信息", subTitle = "提现", operateType = OperateType.OTHER)
    @PostMapping("/withdrawal")
    @CheckSystemPerms("tenant:withdrawal")
    public Result<Void> withdrawal(@RequestBody @Valid WithdrawalReq req) {
        SysTenant sysTenant = tenantMapper.selectById(SystemUserUtil.getTenantId());
        if (!passwordEncoder.matches(req.getOperPwd(), sysTenant.getOperPwd())) {
            return err412("密码错误");
        }
        // TODO 实际提现
        return ok();
    }

}
