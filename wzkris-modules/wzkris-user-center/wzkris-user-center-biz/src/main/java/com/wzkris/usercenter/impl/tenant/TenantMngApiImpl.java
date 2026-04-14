package com.wzkris.usercenter.impl.tenant;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wzkris.common.core.constant.CommonConstants;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.SpringUtil;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.orm.model.AbstractApi;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.security.utils.SecurityUtil;
import com.wzkris.common.web.utils.BeanUtil;
import com.wzkris.usercenter.api.tenant.TenantMngApi;
import com.wzkris.usercenter.domain.TenantInfoDO;
import com.wzkris.usercenter.domain.TenantPackageInfoDO;
import com.wzkris.usercenter.event.CreateTenantEvent;
import com.wzkris.usercenter.mapper.TenantInfoMapper;
import com.wzkris.usercenter.request.PwdResetRequest;
import com.wzkris.usercenter.request.StatusUpdateRequest;
import com.wzkris.usercenter.request.tenant.TenantMngQueryRequest;
import com.wzkris.usercenter.request.tenant.TenantMngSaveRequest;
import com.wzkris.usercenter.request.tenant.TenantMngUpdateRequest;
import com.wzkris.usercenter.response.SelectResponse;
import com.wzkris.usercenter.response.tenant.TenantMngQueryResponse;
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
    public Result<Page<TenantMngQueryResponse>> queryPage(TenantMngQueryRequest request) {
        startPage();
        List<TenantMngQueryResponse> list = tenantInfoMapper.selectVOList(this.buildQueryWrapper(request));
        return getPageResult(list);
    }

    private QueryWrapper<TenantInfoDO> buildQueryWrapper(TenantMngQueryRequest request) {
        return new QueryWrapper<TenantInfoDO>()
                .like(StringUtil.isNotEmpty(request.getTenantName()), "tenant_name", request.getTenantName())
                .eq(StringUtil.isNotEmpty(request.getStatus()), "t.status", request.getStatus())
                .orderByDesc("t.tenant_id");
    }

    @Override
    public Result<TenantMngQueryResponse> queryInfo(Long tenantId) {
        return ok(tenantInfoMapper.selectMngVOById(tenantId));
    }

    @Override
    public Result<Page<SelectResponse>> querySelectPage(String tenantName) {
        LambdaQueryWrapper<TenantInfoDO> lqw = new LambdaQueryWrapper<TenantInfoDO>()
                .select(TenantInfoDO::getTenantId, TenantInfoDO::getTenantName)
                .like(StringUtil.isNotBlank(tenantName), TenantInfoDO::getTenantName, tenantName)
                .orderByAsc(TenantInfoDO::getTenantId);
        startPage();
        List<SelectResponse> list = tenantInfoService.list(lqw).stream().map(tenantInfoDO -> {
            SelectResponse SelectResponse = new SelectResponse();
            SelectResponse.setId(tenantInfoDO.getTenantId());
            SelectResponse.setLabel(tenantInfoDO.getTenantName());
            return SelectResponse;
        }).collect(Collectors.toList());
        return getPageResult(list);
    }

    @Override
    public Result<List<SelectResponse>> queryPackageSelect(String packageName) {
        LambdaQueryWrapper<TenantPackageInfoDO> lqw = new LambdaQueryWrapper<TenantPackageInfoDO>()
                .select(TenantPackageInfoDO::getPackageId, TenantPackageInfoDO::getPackageName)
                .eq(TenantPackageInfoDO::getStatus, CommonConstants.STATUS_ENABLE)
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
        TenantInfoDO tenant = BeanUtil.convert(tenantReq, TenantInfoDO.class);
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
        TenantInfoDO tenant = BeanUtil.convert(tenantReq, TenantInfoDO.class);
        tenant.setAdministrator(null);
        tenant.setOperPwd(null);
        return toRes(tenantInfoMapper.updateById(tenant));
    }

    @Override
    public Result<Void> updateStatus(StatusUpdateRequest request) {
        TenantInfoDO update = new TenantInfoDO(request.getId());
        update.setStatus(request.getStatus());
        return toRes(tenantInfoMapper.updateById(update));
    }

    @Override
    public Result<Void> resetOperPwd(PwdResetRequest request) {
        if (StringUtil.length(request.getPassword()) != 6 || !NumberUtils.isCreatable(request.getPassword())) {
            return requestFail("操作密码必须为6位数字");
        }
        TenantInfoDO update = new TenantInfoDO(request.getId());
        update.setOperPwd(passwordEncoder.encode(request.getPassword()));
        return toRes(tenantInfoMapper.updateById(update));
    }

    @Override
    public Result<Void> remove(Long tenantId) {
        return toRes(tenantInfoService.removeTenant(tenantId));
    }

}
