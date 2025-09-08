package com.wzkris.user.controller.tenantwallet;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.core.domain.Result;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.orm.annotation.IgnoreTenant;
import com.wzkris.common.orm.model.BaseController;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.security.annotation.CheckUserPerms;
import com.wzkris.user.domain.TenantWalletRecordDO;
import com.wzkris.user.domain.req.tenantwallet.TenantWalletRecordQueryReq;
import com.wzkris.user.mapper.TenantWalletRecordMapper;
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
@RestController
@IgnoreTenant // 忽略租户隔离
@RequestMapping("/tenant-wallet-manage")
@PreAuthorize("@lu.isSuperTenant()") // 只允许超级租户访问
@RequiredArgsConstructor
public class TenantWalletManageController extends BaseController {

    private final TenantWalletRecordMapper tenantWalletRecordMapper;

    @Operation(summary = "钱包记录列表")
    @GetMapping("/record")
    @CheckUserPerms("user-mod:tenant-wallet-mng:list")
    public Result<Page<TenantWalletRecordDO>> listWalletPage(TenantWalletRecordQueryReq queryReq) {
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
