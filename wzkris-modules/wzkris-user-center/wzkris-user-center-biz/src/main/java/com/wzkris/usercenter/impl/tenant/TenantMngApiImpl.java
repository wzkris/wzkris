package com.wzkris.usercenter.impl.tenant;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.BeanCopierUtil;
import com.wzkris.common.core.utils.SpringUtil;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.orm.request.IdRequest;
import com.wzkris.common.security.utils.SecurityUtil;
import com.wzkris.common.web.model.AbstractApi;
import com.wzkris.usercenter.api.tenant.TenantMngApi;
import com.wzkris.usercenter.api.tenant.request.TenantMngPageRequest;
import com.wzkris.usercenter.api.tenant.request.TenantMngSaveRequest;
import com.wzkris.usercenter.api.tenant.request.TenantMngUpdateRequest;
import com.wzkris.usercenter.api.tenant.response.TenantMngResponse;
import com.wzkris.usercenter.api.tenantpackage.request.TenantPackageMngListRequest;
import com.wzkris.usercenter.domain.TenantInfoDO;
import com.wzkris.usercenter.domain.TenantPackageInfoDO;
import com.wzkris.usercenter.enums.tenantpackage.TenantPackageStatusEnum;
import com.wzkris.usercenter.event.CreateTenantEvent;
import com.wzkris.usercenter.mapper.TenantInfoMapper;
import com.wzkris.usercenter.request.PwdResetRequest;
import com.wzkris.usercenter.response.SelectResponse;
import com.wzkris.usercenter.service.AdminInfoService;
import com.wzkris.usercenter.service.TenantInfoService;
import com.wzkris.usercenter.service.TenantPackageInfoService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TenantMngApiImpl extends AbstractApi implements TenantMngApi {

    private final TenantInfoMapper tenantInfoMapper;

    private final TenantInfoService tenantInfoService;

    private final AdminInfoService adminInfoService;

    private final TenantPackageInfoService tenantPackageInfoService;

    private final PasswordEncoder passwordEncoder;

    @Override
    public Result<Page<TenantMngResponse>> queryPage(TenantMngPageRequest request) {
        IPage<TenantMngResponse> page = tenantInfoMapper.selectVOPage(request.buildPage(), this.buildQueryWrapper(request));
        return ok(Page.of(page));
    }

    private QueryWrapper<TenantInfoDO> buildQueryWrapper(TenantMngPageRequest request) {
        return new QueryWrapper<TenantInfoDO>()
                .apply("t.deleted = false")
                .like(StringUtil.isNotEmpty(request.getTenantName()), "tenant_name", request.getTenantName())
                .eq(request.getStatus() != null, "t.status", request.getStatus())
                .orderByDesc("t.tenant_id");
    }

    @Override
    public Result<TenantMngResponse> queryInfo(IdRequest request) {
        return ok(tenantInfoMapper.selectMngVOById(request.getId()));
    }

    @Override
    public Result<Page<SelectResponse>> querySelectPage(TenantMngPageRequest request) {
        String tenantName = request.getTenantName();
        LambdaQueryWrapper<TenantInfoDO> lqw = new LambdaQueryWrapper<TenantInfoDO>()
                .select(TenantInfoDO::getTenantId, TenantInfoDO::getTenantName)
                .like(StringUtil.isNotBlank(tenantName), TenantInfoDO::getTenantName, tenantName)
                .orderByAsc(TenantInfoDO::getTenantId);
        IPage<TenantInfoDO> page = tenantInfoService.page(request.buildPage(), lqw);
        List<SelectResponse> list = page.getRecords().stream().map(tenantInfoDO -> {
            SelectResponse SelectResponse = new SelectResponse();
            SelectResponse.setId(tenantInfoDO.getTenantId());
            SelectResponse.setLabel(tenantInfoDO.getTenantName());
            return SelectResponse;
        }).collect(Collectors.toList());
        return ok(Page.of(page, list));
    }

    @Override
    public Result<List<SelectResponse>> queryPackageSelect(TenantPackageMngListRequest request) {
        String packageName = request.getPackageName();
        LambdaQueryWrapper<TenantPackageInfoDO> lqw = new LambdaQueryWrapper<TenantPackageInfoDO>()
                .select(TenantPackageInfoDO::getPackageId, TenantPackageInfoDO::getPackageName)
                .eq(TenantPackageInfoDO::getStatus, TenantPackageStatusEnum.ENABLE)
                .like(StringUtil.isNotBlank(packageName), TenantPackageInfoDO::getPackageName, packageName)
                .orderByAsc(TenantPackageInfoDO::getPackageId);
        List<SelectResponse> selectVOS = tenantPackageInfoService.list(lqw)
                .stream()
                .map(packageInfoDO -> {
                    SelectResponse SelectResponse = new SelectResponse();
                    SelectResponse.setId(packageInfoDO.getPackageId());
                    SelectResponse.setLabel(packageInfoDO.getPackageName());
                    return SelectResponse;
                }).toList();
        return ok(selectVOS);
    }

    @Override
    public Result<Void> save(TenantMngSaveRequest tenantReq) {
        if (adminInfoService.existByUsername(null, tenantReq.getUsername())) {
            return requestFail("登录账号'" + tenantReq.getUsername() + "'已存在");
        }
        TenantInfoDO tenant = BeanCopierUtil.copy(tenantReq, TenantInfoDO.class);
        tenant.setStatus(tenantReq.getStatus());
        String operPwd = StringUtil.toStringOrNull(RandomUtils.secure().randomInt(100_000, 999_999));
        tenant.setOperPwd(operPwd);
        String password = RandomStringUtils.secure().nextAlphabetic(8);
        boolean success = tenantInfoService.saveTenant(tenant, tenantReq.getUsername(), password);
        if (success) {
            SpringUtil.getContext()
                    .publishEvent(new CreateTenantEvent(
                            SecurityUtil.getUid(),
                            tenantReq.getUsername(),
                            tenantReq.getTenantName(),
                            password,
                            operPwd));
        }
        return toRes(success);
    }

    @Override
    public Result<Void> update(TenantMngUpdateRequest tenantReq) {
        TenantInfoDO tenant = BeanCopierUtil.copy(tenantReq, TenantInfoDO.class);
        tenant.setStatus(tenantReq.getStatus());
        tenant.setAdministrator(null);
        tenant.setOperPwd(null);
        return toRes(tenantInfoService.updateById(tenant));
    }

    @Override
    public Result<Void> resetOperPwd(PwdResetRequest request) {
        if (StringUtil.length(request.getPassword()) != 6 || !NumberUtils.isCreatable(request.getPassword())) {
            return requestFail("操作密码必须为6位数字");
        }
        TenantInfoDO update = new TenantInfoDO(request.getId());
        update.setOperPwd(passwordEncoder.encode(request.getPassword()));
        return toRes(tenantInfoService.updateById(update));
    }

    @Override
    public Result<Void> remove(IdRequest request) {
        return toRes(tenantInfoService.removeTenant(request.getId()));
    }

}
