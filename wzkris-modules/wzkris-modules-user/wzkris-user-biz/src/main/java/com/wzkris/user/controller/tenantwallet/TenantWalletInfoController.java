package com.wzkris.user.controller.tenantwallet;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.core.domain.Result;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateType;
import com.wzkris.common.orm.annotation.IgnoreTenant;
import com.wzkris.common.orm.model.BaseController;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.security.annotation.CheckUserPerms;
import com.wzkris.common.security.utils.LoginUserUtil;
import com.wzkris.user.domain.TenantInfoDO;
import com.wzkris.user.domain.TenantWalletRecordDO;
import com.wzkris.user.domain.req.tenantwallet.TenantWalletRecordQueryReq;
import com.wzkris.user.domain.req.tenantwallet.WalletWithdrawalReq;
import com.wzkris.user.domain.vo.tenantwallet.TenantWalletInfoVO;
import com.wzkris.user.mapper.TenantInfoMapper;
import com.wzkris.user.mapper.TenantWalletInfoMapper;
import com.wzkris.user.mapper.TenantWalletRecordMapper;
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
@RequestMapping("/tenant-wallet")
@CheckUserPerms("user-mod:tenant-wallet-info")
@IgnoreTenant(value = false, forceTenantId = "@lu.getTenantId()") // 忽略切换
@RequiredArgsConstructor
public class TenantWalletInfoController extends BaseController {

    private final TenantInfoMapper tenantInfoMapper;

    private final TenantWalletInfoMapper tenantWalletInfoMapper;

    private final TenantWalletRecordMapper tenantWalletRecordMapper;

    private final PasswordEncoder passwordEncoder;

    @Operation(summary = "余额信息")
    @GetMapping
    public Result<TenantWalletInfoVO> walletInfo() {
        return ok(tenantWalletInfoMapper.selectById2VO(LoginUserUtil.getTenantId(), TenantWalletInfoVO.class));
    }

    @Operation(summary = "钱包记录")
    @GetMapping("/record")
    public Result<Page<TenantWalletRecordDO>> listWalletPage(TenantWalletRecordQueryReq queryReq) {
        startPage();
        List<TenantWalletRecordDO> recordList =
                tenantWalletRecordMapper.selectList(this.buildWalletQueryWrapper(queryReq));
        return getDataTable(recordList);
    }

    private LambdaQueryWrapper<TenantWalletRecordDO> buildWalletQueryWrapper(TenantWalletRecordQueryReq queryReq) {
        return new LambdaQueryWrapper<TenantWalletRecordDO>()
                .like(
                        StringUtil.isNotBlank(queryReq.getRecordType()),
                        TenantWalletRecordDO::getRecordType,
                        queryReq.getRecordType())
                .between(
                        queryReq.getParam("beginTime") != null && queryReq.getParam("endTime") != null,
                        TenantWalletRecordDO::getCreateAt,
                        queryReq.getParam("beginTime"),
                        queryReq.getParam("endTime"))
                .orderByDesc(TenantWalletRecordDO::getRecordId);
    }

    @Operation(summary = "提现")
    @OperateLog(title = "商户信息", subTitle = "提现", operateType = OperateType.OTHER)
    @PostMapping("/withdrawal")
    @CheckUserPerms("user-mod:tenant-wallet-info:withdrawal")
    public Result<Void> withdrawal(@RequestBody @Valid WalletWithdrawalReq req) {
        TenantInfoDO sysTenant = tenantInfoMapper.selectById(LoginUserUtil.getTenantId());
        if (!passwordEncoder.matches(req.getOperPwd(), sysTenant.getOperPwd())) {
            return err40000("密码错误");
        }
        // TODO 实际提现
        return ok();
    }

}
