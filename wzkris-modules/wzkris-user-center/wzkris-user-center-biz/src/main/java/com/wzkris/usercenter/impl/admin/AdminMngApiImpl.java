package com.wzkris.usercenter.impl.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.SpringUtil;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.excel.utils.ExcelUtil;
import com.wzkris.common.orm.model.AbstractApi;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.orm.request.IdListRequest;
import com.wzkris.common.orm.request.IdRequest;
import com.wzkris.common.security.utils.SecurityUtil;
import com.wzkris.common.web.utils.BeanUtil;
import com.wzkris.usercenter.api.admin.AdminMngApi;
import com.wzkris.usercenter.api.admin.request.*;
import com.wzkris.usercenter.api.admin.response.AdminInfoExportResponse;
import com.wzkris.usercenter.api.admin.response.AdminMngResponse;
import com.wzkris.usercenter.api.dept.request.DeptMngListRequest;
import com.wzkris.usercenter.domain.AdminInfoDO;
import com.wzkris.usercenter.event.CreateAdminEvent;
import com.wzkris.usercenter.mapper.AdminInfoMapper;
import com.wzkris.usercenter.mapper.RoleInfoMapper;
import com.wzkris.usercenter.request.PwdResetRequest;
import com.wzkris.usercenter.response.CheckedSelectResponse;
import com.wzkris.usercenter.response.SelectTreeResponse;
import com.wzkris.usercenter.service.AdminInfoService;
import com.wzkris.usercenter.service.DeptInfoService;
import com.wzkris.usercenter.service.RoleInfoService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminMngApiImpl extends AbstractApi implements AdminMngApi {

    private final AdminInfoMapper adminInfoMapper;

    private final AdminInfoService adminInfoService;

    private final RoleInfoService roleInfoService;

    private final DeptInfoService deptInfoService;

    private final PasswordEncoder passwordEncoder;

    private final RoleInfoMapper roleInfoMapper;

    @Override
    public Result<Page<AdminMngResponse>> queryPage(AdminMngPageRequest request) {
        startPage(request);
        List<AdminMngResponse> list = adminInfoMapper.selectVOList(this.buildPageWrapper(request));
        return getPageResult(list);
    }

    private QueryWrapper<AdminInfoDO> buildPageWrapper(AdminMngPageRequest request) {
        return new QueryWrapper<AdminInfoDO>()
                .like(ObjectUtils.isNotEmpty(request.getUsername()), "username", request.getUsername())
                .like(ObjectUtils.isNotEmpty(request.getNickname()), "nickname", request.getNickname())
                .like(ObjectUtils.isNotEmpty(request.getPhoneNumber()), "phone_number", request.getPhoneNumber())
                .like(ObjectUtils.isNotEmpty(request.getEmail()), "u.email", request.getEmail())
                .eq(ObjectUtils.isNotEmpty(request.getStatus()), "u.status", request.getStatus())
                .eq(ObjectUtils.isNotEmpty(request.getDeptId()), "u.dept_id", request.getDeptId())
                .between(request.getBeginTime() != null && request.getEndTime() != null,
                        "u.create_at",
                        request.getBeginTime(),
                        request.getEndTime())
                .orderByDesc("u.admin_id");
    }

    @Override
    public Result<List<SelectTreeResponse>> queryDeptSelectTree(DeptMngListRequest request) {
        String deptName = request.getDeptName();
        return ok(deptInfoService.listSelectTree(deptName));
    }

    @Override
    public Result<CheckedSelectResponse> queryRoleSelect(AdminMngRoleSelectRequest request) {
        Long adminId = request.getAdminId();
        if (!adminInfoMapper.checkDataScopes(adminId)) {
            return accessDenied("数据权限不足");
        }
        CheckedSelectResponse checkedSelectResponse = new CheckedSelectResponse();
        checkedSelectResponse.setCheckedKeys(adminId == null ? Collections.emptyList() : roleInfoService.listIdByAdminId(adminId));
        checkedSelectResponse.setSelects(roleInfoService.listRoleSelect(request.getRoleName()));
        return ok(checkedSelectResponse);
    }

    @Override
    public Result<AdminMngResponse> queryInfo(IdRequest request) {
        Long adminId = request.getId();
        if (!adminInfoMapper.checkDataScopes(adminId)) {
            return accessDenied("数据权限不足");
        }
        return ok(BeanUtil.convert(adminInfoMapper.selectById(adminId), AdminMngResponse.class));
    }

    @Override
    public Result<Void> save(AdminMngSaveRequest request) {
        if (adminInfoService.existByUsername(null, request.getUsername())) {
            return requestFail("添加管理员'" + request.getUsername() + "'失败，登录账号已存在");
        } else if (StringUtil.isNotEmpty(request.getPhoneNumber())
                && adminInfoService.existByPhoneNumber(null, request.getPhoneNumber())) {
            return requestFail("添加管理员'" + request.getUsername() + "'失败，手机号码已存在");
        }
        AdminInfoDO admin = BeanUtil.convert(request, AdminInfoDO.class);
        admin.setStatus(request.getStatus());
        String password = RandomStringUtils.secure().nextAlphabetic(8);
        admin.setPassword(password);
        boolean success = adminInfoService.saveAdmin(admin, request.getRoleIds());
        if (success) {
            SpringUtil.getContext()
                    .publishEvent(new CreateAdminEvent(SecurityUtil.getUid(), request.getUsername(), password));
        }
        return toRes(success);
    }

    @Override
    public Result<Void> update(AdminMngUpdateRequest request) {
        if (!adminInfoMapper.checkDataScopes(request.getAdminId())) {
            return accessDenied("数据权限不足");
        }
        if (adminInfoService.existByUsername(request.getAdminId(), request.getUsername())) {
            return requestFail("修改管理员'" + request.getUsername() + "'失败，登录账号已存在");
        } else if (StringUtil.isNotEmpty(request.getPhoneNumber())
                && adminInfoService.existByPhoneNumber(request.getAdminId(), request.getPhoneNumber())) {
            return requestFail("修改管理员'" + request.getUsername() + "'失败，手机号码已存在");
        }
        AdminInfoDO admin = BeanUtil.convert(request, AdminInfoDO.class);
        admin.setStatus(request.getStatus());
        return toRes(adminInfoService.updateAdmin(admin, request.getRoleIds()));
    }

    @Override
    public Result<Void> grantRoles(AdminMngGrantRequest request) {
        if (!adminInfoMapper.checkDataScopes(request.getAdminId())) {
            return accessDenied("数据权限不足");
        }
        if (!roleInfoMapper.checkDataScopes(request.getRoleIds())) {
            return accessDenied("数据权限不足");
        }
        return toRes(adminInfoService.grantRoles(request.getAdminId(), request.getRoleIds()));
    }

    @Override
    public Result<Void> remove(IdListRequest request) {
        List<Long> userIds = request.getIds();
        if (!adminInfoMapper.checkDataScopes(userIds)) {
            return accessDenied("数据权限不足");
        }
        return toRes(adminInfoService.removeAdmins(userIds));
    }

    @Override
    public Result<Void> resetPwd(PwdResetRequest request) {
        if (!adminInfoMapper.checkDataScopes(request.getId())) {
            return accessDenied("数据权限不足");
        }
        AdminInfoDO update = new AdminInfoDO(request.getId());
        update.setPassword(passwordEncoder.encode(request.getPassword()));
        return toRes(adminInfoMapper.updateById(update));
    }

    @Override
    public void export(HttpServletResponse response, AdminMngPageRequest request) {
        List<AdminMngResponse> list = adminInfoMapper.selectVOList(this.buildPageWrapper(request));
        List<AdminInfoExportResponse> convert = BeanUtil.convert(list, AdminInfoExportResponse.class);
        ExcelUtil.exportExcel(convert, "后台管理员数据", AdminInfoExportResponse.class, false, response, null);
    }

}
