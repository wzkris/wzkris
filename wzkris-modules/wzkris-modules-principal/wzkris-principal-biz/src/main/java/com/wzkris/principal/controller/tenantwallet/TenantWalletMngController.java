package com.wzkris.principal.controller.tenantwallet;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.orm.model.BaseController;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.security.annotation.CheckAdminPerms;
import com.wzkris.principal.domain.TenantWalletRecordDO;
import com.wzkris.principal.domain.req.tenantwallet.TenantWalletRecordQueryReq;
import com.wzkris.principal.mapper.TenantWalletRecordMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 租户钱包管理
 *
 * @author wzkris
 */
@Tag(name = "租户钱包管理")
@Validated
@RestController
@RequestMapping("/tenant-wallet-manage")
@RequiredArgsConstructor
public class TenantWalletMngController extends BaseController {

    private final TenantWalletRecordMapper tenantWalletRecordMapper;

    @Operation(summary = "钱包记录分页")
    @GetMapping("/record/page")
    @CheckAdminPerms("prin-mod:tenant-wallet-mng:record-page")
    public Result<Page<TenantWalletRecordDO>> pageRecord(TenantWalletRecordQueryReq queryReq) {
        startPage();
        List<TenantWalletRecordDO> recordList =
                tenantWalletRecordMapper.selectList(this.buildWalletQueryWrapper(queryReq));
        return getDataTable(recordList);
    }

    private LambdaQueryWrapper<TenantWalletRecordDO> buildWalletQueryWrapper(TenantWalletRecordQueryReq queryReq) {
        return new LambdaQueryWrapper<TenantWalletRecordDO>()
                .eq(
                        ObjectUtils.isNotEmpty(queryReq.getTenantId()),
                        TenantWalletRecordDO::getTenantId,
                        queryReq.getTenantId())
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

}
