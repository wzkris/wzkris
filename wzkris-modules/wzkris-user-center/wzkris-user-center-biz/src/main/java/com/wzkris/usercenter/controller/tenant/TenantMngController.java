package com.wzkris.usercenter.controller.tenant;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.SpringUtil;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateTypeEnum;
import com.wzkris.common.orm.model.BaseController;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.security.annotation.CheckAdminPerms;
import com.wzkris.common.security.enums.CheckMode;
import com.wzkris.common.security.utils.AdminUtil;
import com.wzkris.common.validator.group.ValidationGroups;
import com.wzkris.common.web.utils.BeanUtil;
import com.wzkris.usercenter.domain.TenantInfoDO;
import com.wzkris.usercenter.domain.req.EditStatusReq;
import com.wzkris.usercenter.domain.req.ResetPwdReq;
import com.wzkris.usercenter.domain.req.tenant.TenantMngQueryReq;
import com.wzkris.usercenter.domain.req.tenant.TenantMngReq;
import com.wzkris.usercenter.domain.vo.SelectVO;
import com.wzkris.usercenter.domain.vo.tenant.TenantMngVO;
import com.wzkris.usercenter.listener.event.CreateTenantEvent;
import com.wzkris.usercenter.mapper.TenantInfoMapper;
import com.wzkris.usercenter.service.AdminInfoService;
import com.wzkris.usercenter.service.TenantInfoService;
import com.wzkris.usercenter.service.TenantPackageInfoService;
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
public class TenantMngController extends BaseController {

    private final TenantInfoMapper tenantInfoMapper;

    private final TenantInfoService tenantInfoService;

    private final AdminInfoService adminInfoService;

    private final TenantPackageInfoService tenantPackageInfoService;

    private final PasswordEncoder passwordEncoder;

    @Operation(summary = "租户分页")
    @GetMapping("/page")
    @CheckAdminPerms("user-mod:tenant-mng:page")
    public Result<Page<TenantMngVO>> page(TenantMngQueryReq queryReq) {
        startPage();
        List<TenantMngVO> list = tenantInfoMapper.selectVOList(this.buildQueryWrapper(queryReq));
        return getDataTable(list);
    }

    private QueryWrapper<TenantInfoDO> buildQueryWrapper(TenantMngQueryReq queryReq) {
        return new QueryWrapper<TenantInfoDO>()
                .like(StringUtil.isNotEmpty(queryReq.getTenantName()), "tenant_name", queryReq.getTenantName())
                .eq(StringUtil.isNotEmpty(queryReq.getStatus()), "t.status", queryReq.getStatus())
                .orderByDesc("t.tenant_id");
    }

    @Operation(summary = "ID获取租户详细信息")
    @GetMapping("/{tenantId}")
    @CheckAdminPerms("user-mod:tenant-mng:page")
    public Result<TenantInfoDO> queryByid(
            @NotNull(message = "{invalidParameter.id.invalid}") @PathVariable Long tenantId) {
        return ok(tenantInfoMapper.selectById(tenantId));
    }

    @Operation(summary = "租户选择列表(带分页)")
    @GetMapping("/selectpage")
    public Result<Page<SelectVO>> selectlist(String tenantName) {
        startPage();
        List<SelectVO> list = tenantInfoService.listSelect(tenantName);
        return getDataTable(list);
    }

    @Operation(summary = "套餐选择列表")
    @GetMapping("/package-select")
    @CheckAdminPerms(
            value = {"user-mod:tenant-mng:add", "user-mod:tenant-mng:edit"},
            mode = CheckMode.OR)
    public Result<List<SelectVO>> packageSelect(String packageName) {
        List<SelectVO> selectVOS = tenantPackageInfoService.listSelect(packageName);
        return ok(selectVOS);
    }

    @Operation(summary = "新增租户")
    @OperateLog(title = "租户管理", subTitle = "新增租户", type = OperateTypeEnum.INSERT)
    @PostMapping("/add")
    @CheckAdminPerms("user-mod:tenant-mng:add")
    public Result<Void> add(@Validated(ValidationGroups.Insert.class) @RequestBody TenantMngReq tenantReq) {
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
    @OperateLog(title = "租户管理", subTitle = "修改租户", type = OperateTypeEnum.UPDATE)
    @PostMapping("/edit")
    @CheckAdminPerms("user-mod:tenant-mng:edit")
    public Result<Void> edit(@Validated @RequestBody TenantMngReq tenantReq) {
        TenantInfoDO tenant = BeanUtil.convert(tenantReq, TenantInfoDO.class);
        tenant.setAdministrator(null);
        tenant.setOperPwd(null);
        return toRes(tenantInfoMapper.updateById(tenant));
    }

    @Operation(summary = "修改租户状态")
    @OperateLog(title = "租户管理", subTitle = "修改租户状态", type = OperateTypeEnum.UPDATE)
    @PostMapping("/edit-status")
    @CheckAdminPerms("user-mod:tenant-mng:edit")
    public Result<Void> editStatus(@RequestBody @Valid EditStatusReq statusReq) {
        TenantInfoDO update = new TenantInfoDO(statusReq.getId());
        update.setStatus(statusReq.getStatus());
        return toRes(tenantInfoMapper.updateById(update));
    }

    @Operation(summary = "重置租户操作密码")
    @OperateLog(title = "租户管理", subTitle = "重置操作密码", type = OperateTypeEnum.UPDATE)
    @PostMapping("/reset-operpwd")
    @CheckAdminPerms("user-mod:tenant-mng:reset-operpwd")
    public Result<Void> resetOperPwd(@RequestBody ResetPwdReq req) {
        if (StringUtil.length(req.getPassword()) != 6 || !NumberUtils.isCreatable(req.getPassword())) {
            return err40000("操作密码必须为6位数字");
        }
        TenantInfoDO update = new TenantInfoDO(req.getId());
        update.setOperPwd(passwordEncoder.encode(req.getPassword()));
        return toRes(tenantInfoMapper.updateById(update));
    }

    @Operation(summary = "删除租户")
    @OperateLog(title = "租户管理", subTitle = "删除租户", type = OperateTypeEnum.DELETE)
    @PostMapping("/remove")
    @CheckAdminPerms("user-mod:tenant-mng:remove")
    public Result<Void> remove(@RequestBody @NotNull(message = "{invalidParameter.id.invalid}") Long tenantId) {
        return toRes(tenantInfoService.removeById(tenantId));
    }

}
