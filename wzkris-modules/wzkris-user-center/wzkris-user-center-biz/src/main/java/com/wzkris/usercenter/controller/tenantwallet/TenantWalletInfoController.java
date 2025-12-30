package com.wzkris.usercenter.controller.tenantwallet;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateTypeEnum;
import com.wzkris.common.orm.model.BaseController;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.security.annotation.CheckTenantPerms;
import com.wzkris.common.security.utils.TenantUtil;
import com.wzkris.usercenter.domain.TenantInfoDO;
import com.wzkris.usercenter.domain.TenantWalletRecordDO;
import com.wzkris.usercenter.domain.req.tenantwallet.TenantWalletRecordQueryReq;
import com.wzkris.usercenter.domain.req.tenantwallet.WalletWithdrawalReq;
import com.wzkris.usercenter.domain.vo.tenantwallet.TenantWalletInfoVO;
import com.wzkris.usercenter.mapper.TenantInfoMapper;
import com.wzkris.usercenter.mapper.TenantWalletInfoMapper;
import com.wzkris.usercenter.mapper.TenantWalletRecordMapper;
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
@CheckTenantPerms("user-mod:tenant-wallet-info")
@RequiredArgsConstructor
public class TenantWalletInfoController extends BaseController {

    private final TenantInfoMapper tenantInfoMapper;

    private final TenantWalletInfoMapper tenantWalletInfoMapper;

    private final TenantWalletRecordMapper tenantWalletRecordMapper;

    private final PasswordEncoder passwordEncoder;

    @Operation(summary = "余额信息")
    @GetMapping("/info")
    public Result<TenantWalletInfoVO> walletInfo() {
        return ok(tenantWalletInfoMapper.selectById2VO(TenantUtil.getTenantId(), TenantWalletInfoVO.class));
    }

    @Operation(summary = "钱包记录分页")
    @GetMapping("/record/page")
    public Result<Page<TenantWalletRecordDO>> pageRecord(TenantWalletRecordQueryReq queryReq) {
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
    @OperateLog(title = "商户信息", subTitle = "提现", type = OperateTypeEnum.OTHER)
    @PostMapping("/withdrawal")
    @CheckTenantPerms("user-mod:tenant-wallet-info:withdrawal")
    public Result<Void> withdrawal(@RequestBody @Valid WalletWithdrawalReq req) {
        TenantInfoDO sysTenant = tenantInfoMapper.selectById(TenantUtil.getTenantId());
        if (!passwordEncoder.matches(req.getOperPwd(), sysTenant.getOperPwd())) {
            return requestFail("密码错误");
        }
        // TODO 实际提现
        return ok();
    }

}
