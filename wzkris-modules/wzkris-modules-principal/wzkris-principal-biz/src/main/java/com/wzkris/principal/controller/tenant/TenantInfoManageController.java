package com.wzkris.principal.controller.tenant;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.SpringUtil;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateType;
import com.wzkris.common.orm.model.BaseController;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.security.annotation.CheckUserPerms;
import com.wzkris.common.security.annotation.enums.CheckMode;
import com.wzkris.common.security.utils.AdminUtil;
import com.wzkris.common.validator.group.ValidationGroups;
import com.wzkris.common.web.utils.BeanUtil;
import com.wzkris.principal.domain.TenantInfoDO;
import com.wzkris.principal.domain.req.EditStatusReq;
import com.wzkris.principal.domain.req.ResetPwdReq;
import com.wzkris.principal.domain.req.tenant.TenantManageQueryReq;
import com.wzkris.principal.domain.req.tenant.TenantManageReq;
import com.wzkris.principal.domain.vo.SelectVO;
import com.wzkris.principal.domain.vo.tenant.TenantManageVO;
import com.wzkris.principal.listener.event.CreateTenantEvent;
import com.wzkris.principal.mapper.TenantInfoMapper;
import com.wzkris.principal.service.AdminInfoService;
import com.wzkris.principal.service.TenantInfoService;
import com.wzkris.principal.service.TenantPackageInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 租户管理
 *
 * @author wzkris
 */
@Tag(name = "租户管理")
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/tenant-manage")
public class TenantInfoManageController extends BaseController {

    private final TenantInfoMapper tenantInfoMapper;

    private final TenantInfoService tenantInfoService;

    private final AdminInfoService adminInfoService;

    private final TenantPackageInfoService tenantPackageInfoService;

    private final PasswordEncoder passwordEncoder;

    @Operation(summary = "租户分页")
    @GetMapping("/list")
    @CheckUserPerms("prin-mod:tenant-mng:list")
    public Result<Page<TenantManageVO>> listPage(TenantManageQueryReq queryReq) {
        startPage();
        List<TenantManageVO> list = tenantInfoMapper.selectVOList(this.buildQueryWrapper(queryReq));
        return getDataTable(list);
    }

    private QueryWrapper<TenantInfoDO> buildQueryWrapper(TenantManageQueryReq queryReq) {
        return new QueryWrapper<TenantInfoDO>()
                .like(StringUtil.isNotEmpty(queryReq.getTenantName()), "tenant_name", queryReq.getTenantName())
                .eq(StringUtil.isNotEmpty(queryReq.getStatus()), "t.status", queryReq.getStatus())
                .orderByDesc("t.tenant_id");
    }

    @Operation(summary = "ID获取租户详细信息")
    @GetMapping("/{tenantId}")
    @CheckUserPerms("prin-mod:tenant-mng:list")
    public Result<TenantInfoDO> queryByid(
            @NotNull(message = "{invalidParameter.id.invalid}") @PathVariable Long tenantId) {
        return ok(tenantInfoMapper.selectById(tenantId));
    }

    @Operation(summary = "租户选择列表(带分页)")
    @GetMapping("/selectlist")
    public Result<Page<SelectVO>> selectlist(String tenantName) {
        startPage();
        List<SelectVO> list = tenantInfoService.listSelect(tenantName);
        return getDataTable(list);
    }

    @Operation(summary = "套餐选择列表")
    @GetMapping("/package-select")
    @CheckUserPerms(
            value = {"prin-mod:tenant-mng:add", "prin-mod:tenant-mng:edit"},
            mode = CheckMode.OR)
    public Result<List<SelectVO>> packageSelect(String packageName) {
        List<SelectVO> selectVOS = tenantPackageInfoService.listSelect(packageName);
        return ok(selectVOS);
    }

    @Operation(summary = "新增租户")
    @OperateLog(title = "租户管理", subTitle = "新增租户", operateType = OperateType.INSERT)
    @PostMapping("/add")
    @CheckUserPerms("prin-mod:tenant-mng:add")
    public Result<Void> add(@Validated(ValidationGroups.Insert.class) @RequestBody TenantManageReq tenantReq) {
        if (adminInfoService.existByUsername(null, tenantReq.getUsername())) {
            return err40000("登录账号'" + tenantReq.getUsername() + "'已存在");
        }
        TenantInfoDO tenant = BeanUtil.convert(tenantReq, TenantInfoDO.class);

        String operPwd = StringUtil.toStringOrNull(RandomUtils.secure().randomInt(100_000, 999_999));
        tenant.setOperPwd(operPwd);

        String password = RandomStringUtils.secure().nextAlphabetic(8);
        boolean success = tenantInfoService.saveTenant(tenant, tenantReq.getUsername(), password);
        if (success) {
            SpringUtil.getContext()
                    .publishEvent(new CreateTenantEvent(
                            AdminUtil.getId(),
                            tenantReq.getUsername(),
                            tenantReq.getTenantName(),
                            password,
                            operPwd));
        }
        return toRes(success);
    }

    @Operation(summary = "修改租户")
    @OperateLog(title = "租户管理", subTitle = "修改租户", operateType = OperateType.UPDATE)
    @PostMapping("/edit")
    @CheckUserPerms("prin-mod:tenant-mng:edit")
    public Result<Void> edit(@Validated @RequestBody TenantManageReq tenantReq) {
        TenantInfoDO tenant = BeanUtil.convert(tenantReq, TenantInfoDO.class);
        tenant.setAdministrator(null);
        tenant.setOperPwd(null);
        return toRes(tenantInfoMapper.updateById(tenant));
    }

    @Operation(summary = "修改租户状态")
    @OperateLog(title = "租户管理", subTitle = "修改租户状态", operateType = OperateType.UPDATE)
    @PostMapping("/edit-status")
    @CheckUserPerms("prin-mod:tenant-mng:edit")
    public Result<Void> editStatus(@RequestBody @Valid EditStatusReq statusReq) {
        TenantInfoDO update = new TenantInfoDO(statusReq.getId());
        update.setStatus(statusReq.getStatus());
        return toRes(tenantInfoMapper.updateById(update));
    }

    @Operation(summary = "重置租户操作密码")
    @OperateLog(title = "租户管理", subTitle = "重置操作密码", operateType = OperateType.UPDATE)
    @PostMapping("/reset-operpwd")
    @CheckUserPerms("prin-mod:tenant-mng:reset-operpwd")
    public Result<Void> resetOperPwd(@RequestBody ResetPwdReq req) {
        if (StringUtil.length(req.getPassword()) != 6 || !NumberUtils.isCreatable(req.getPassword())) {
            return err40000("操作密码必须为6位数字");
        }
        TenantInfoDO update = new TenantInfoDO(req.getId());
        update.setOperPwd(passwordEncoder.encode(req.getPassword()));
        return toRes(tenantInfoMapper.updateById(update));
    }

    @Operation(summary = "删除租户")
    @OperateLog(title = "租户管理", subTitle = "删除租户", operateType = OperateType.DELETE)
    @PostMapping("/remove")
    @CheckUserPerms("prin-mod:tenant-mng:remove")
    public Result<Void> remove(@RequestBody @NotNull(message = "{invalidParameter.id.invalid}") Long tenantId) {
        return toRes(tenantInfoService.removeById(tenantId));
    }

}
