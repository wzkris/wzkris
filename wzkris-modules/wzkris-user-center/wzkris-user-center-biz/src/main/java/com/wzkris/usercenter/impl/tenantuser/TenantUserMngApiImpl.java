package com.wzkris.usercenter.impl.tenantuser;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.BeanCopierUtil;
import com.wzkris.common.core.utils.SpringUtil;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.orm.request.IdListRequest;
import com.wzkris.common.orm.request.IdRequest;
import com.wzkris.common.security.utils.SecurityUtil;
import com.wzkris.common.web.model.AbstractApi;
import com.wzkris.usercenter.api.tenantuser.TenantUserMngApi;
import com.wzkris.usercenter.api.tenantuser.request.*;
import com.wzkris.usercenter.api.tenantuser.response.TenantUserMngPageResponse;
import com.wzkris.usercenter.api.tenantuser.response.TenantUserMngQueryResponse;
import com.wzkris.usercenter.domain.TenantUserDO;
import com.wzkris.usercenter.domain.TenantRoleDO;
import com.wzkris.usercenter.event.CreateTenantUserEvent;
import com.wzkris.usercenter.mapper.TenantUserMapper;
import com.wzkris.usercenter.request.PwdResetRequest;
import com.wzkris.usercenter.response.CheckedSelectResponse;
import com.wzkris.usercenter.service.TenantUserService;
import com.wzkris.usercenter.service.TenantRoleService;
import com.wzkris.usercenter.service.TenantInfoService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TenantUserMngApiImpl extends AbstractApi implements TenantUserMngApi {

    private final TenantUserMapper tenantUserMapper;

    private final TenantUserService tenantUserService;

    private final TenantRoleService tenantRoleService;

    private final TenantInfoService tenantInfoService;

    private final PasswordEncoder passwordEncoder;

    @Override
    public Result<Page<TenantUserMngPageResponse>> queryPage(TenantUserMngPageRequest request) {
        IPage<TenantUserMngPageResponse> page = tenantUserMapper.selectVOPage(request.buildPage(), this.buildPageWrapper(request));
        return ok(Page.of(page));
    }

    private QueryWrapper<TenantUserDO> buildPageWrapper(TenantUserMngPageRequest request) {
        return new QueryWrapper<TenantUserDO>()
                .apply("s.deleted = false")
                .like(ObjectUtils.isNotEmpty(request.getUsername()), "username", request.getUsername())
                .like(ObjectUtils.isNotEmpty(request.getPhoneNumber()), "phone_number", request.getPhoneNumber())
                .eq(ObjectUtils.isNotEmpty(request.getStatus()), "s.status", request.getStatus())
                .between(request.getBeginTime() != null && request.getEndTime() != null,
                        "s.create_at",
                        request.getBeginTime(), request.getEndTime());
    }

    @Override
    public Result<TenantUserMngQueryResponse> queryById(IdRequest request) {
        Long tenantUserId = request.getId();
        if (tenantInfoService.checkAdministrator(tenantUserId)) {
            return accessDenied("数据权限不足");
        }
        return ok(BeanCopierUtil.copy(tenantUserService.getById(tenantUserId), TenantUserMngQueryResponse.class));
    }

    @Override
    public Result<CheckedSelectResponse> queryRoleSelect(TenantUserMngRoleSelectRequest request) {
        Long tenantUserId = request.getId();
        CheckedSelectResponse checkedSelectResponse = new CheckedSelectResponse();
        checkedSelectResponse.setCheckedKeys(tenantUserId == null ? Collections.emptyList() : tenantRoleService.listByTenantUserId(tenantUserId).stream().map(TenantRoleDO::getId).toList());
        checkedSelectResponse.setSelects(tenantRoleService.listSelect(request.getRoleName()));
        return ok(checkedSelectResponse);
    }

    @Override
    public Result<Void> save(TenantUserMngSaveRequest tenantUserReq) {
        if (!tenantInfoService.checkAccountLimit(SecurityUtil.getTenantId())) {
            return requestFail("账号数量已达上限，请联系管理员");
        } else if (tenantUserService.existByUsername(null, tenantUserReq.getUsername())) {
            return requestFail("添加用户'" + tenantUserReq.getUsername() + "'失败，登录账号已存在");
        } else if (StringUtil.isNotEmpty(tenantUserReq.getPhoneNumber())
                && tenantUserService.existByPhoneNumber(null, tenantUserReq.getPhoneNumber())) {
            return requestFail("添加用户'" + tenantUserReq.getUsername() + "'失败，手机号码已存在");
        }
        TenantUserDO tenantUser = BeanCopierUtil.copy(tenantUserReq, TenantUserDO.class);
        tenantUser.setStatus(tenantUserReq.getStatus());
        String password = RandomStringUtils.secure().nextAlphabetic(8);
        tenantUser.setPassword(password);
        boolean success = tenantUserService.saveTenantUser(tenantUser, tenantUserReq.getTenantRoleIds());
        if (success) {
            SpringUtil.getContext()
                    .publishEvent(new CreateTenantUserEvent(SecurityUtil.getUid(), tenantUserReq.getUsername(), password));
        }
        return toRes(success);
    }

    @Override
    public Result<Void> update(TenantUserMngUpdateRequest tenantUserReq) {
        if (tenantInfoService.checkAdministrator(tenantUserReq.getId())) {
            return accessDenied("数据权限不足");
        }
        if (tenantUserService.existByUsername(tenantUserReq.getId(), tenantUserReq.getUsername())) {
            return requestFail("修改用户'" + tenantUserReq.getUsername() + "'失败，登录账号已存在");
        } else if (StringUtil.isNotEmpty(tenantUserReq.getPhoneNumber())
                && tenantUserService.existByPhoneNumber(tenantUserReq.getId(), tenantUserReq.getPhoneNumber())) {
            return requestFail("修改用户'" + tenantUserReq.getUsername() + "'失败，手机号码已存在");
        }
        TenantUserDO tenantUser = BeanCopierUtil.copy(tenantUserReq, TenantUserDO.class);
        tenantUser.setStatus(tenantUserReq.getStatus());
        return toRes(tenantUserService.updateTenantUser(tenantUser, tenantUserReq.getTenantRoleIds()));
    }

    @Override
    public Result<Void> resetPwd(PwdResetRequest request) {
        if (tenantInfoService.checkAdministrator(request.getId())) {
            return accessDenied("数据权限不足");
        }
        TenantUserDO update = new TenantUserDO(request.getId());
        update.setPassword(passwordEncoder.encode(request.getPassword()));
        return toRes(tenantUserService.updateById(update));
    }

    @Override
    public Result<Void> grantRoles(TenantUserMngGrantRoleRequest request) {
        if (tenantInfoService.checkAdministrator(request.getId())) {
            return accessDenied("数据权限不足");
        }
        return toRes(tenantUserService.grantRoles(request.getId(), request.getTenantRoleIds()));
    }

    @Override
    public Result<Void> remove(IdListRequest request) {
        List<Long> tenantUserIds = request.getIdList();
        if (tenantInfoService.checkAdministrator(tenantUserIds)) {
            return accessDenied("数据权限不足");
        }
        return toRes(tenantUserService.removeTenantUsers(tenantUserIds));
    }

}
