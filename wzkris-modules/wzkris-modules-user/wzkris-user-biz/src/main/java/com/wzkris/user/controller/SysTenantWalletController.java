package com.wzkris.user.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.core.domain.Result;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.orm.annotation.IgnoreTenant;
import com.wzkris.common.orm.model.BaseController;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.security.annotation.CheckSystemPerms;
import com.wzkris.user.domain.SysTenantWalletRecord;
import com.wzkris.user.domain.req.SysTenantWalletRecordQueryReq;
import com.wzkris.user.mapper.SysTenantWalletRecordMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.access.prepost.PreAuthorize;
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
@RequiredArgsConstructor
@RestController
@PreAuthorize("@su.isSuperTenant()") // 只允许超级租户访问
@IgnoreTenant // 忽略租户隔离
@RequestMapping("/sys_tenant/wallet")
public class SysTenantWalletController extends BaseController {

    private final SysTenantWalletRecordMapper tenantWalletRecordMapper;

    @Operation(summary = "钱包记录列表")
    @GetMapping("/record")
    @CheckSystemPerms("sys_tenant:wallet_record")
    public Result<Page<SysTenantWalletRecord>> listWalletPage(SysTenantWalletRecordQueryReq queryReq) {
        startPage();
        List<SysTenantWalletRecord> recordList =
                tenantWalletRecordMapper.selectList(this.buildWalletQueryWrapper(queryReq));
        return getDataTable(recordList);
    }

    private LambdaQueryWrapper<SysTenantWalletRecord> buildWalletQueryWrapper(SysTenantWalletRecordQueryReq queryReq) {
        return new LambdaQueryWrapper<SysTenantWalletRecord>()
                .eq(
                        ObjectUtils.isNotEmpty(queryReq.getTenantId()),
                        SysTenantWalletRecord::getTenantId,
                        queryReq.getTenantId())
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

}
