package com.wzkris.usercenter.api.admin;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.Page;
import com.wzkris.usercenter.request.PwdResetRequest;
import com.wzkris.usercenter.request.StatusUpdateRequest;
import com.wzkris.usercenter.request.admin.AdminMngGrantRequest;
import com.wzkris.usercenter.request.admin.AdminMngQueryRequest;
import com.wzkris.usercenter.request.admin.AdminMngSaveRequest;
import com.wzkris.usercenter.request.admin.AdminMngUpdateRequest;
import com.wzkris.usercenter.response.CheckedSelectResponse;
import com.wzkris.usercenter.response.SelectTreeResponse;
import com.wzkris.usercenter.response.admin.AdminMngResponse;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

public interface AdminMngApi {

    Result<Page<AdminMngResponse>> queryPage(AdminMngQueryRequest request);

    Result<List<SelectTreeResponse>> queryDeptSelectTree(String deptName);

    Result<CheckedSelectResponse> queryRoleSelect(Long adminId, String roleName);

    Result<AdminMngResponse> queryInfo(Long adminId);

    Result<Void> save(AdminMngSaveRequest request);

    Result<Void> update(AdminMngUpdateRequest request);

    Result<Void> grantRoles(AdminMngGrantRequest request);

    Result<Void> remove(List<Long> userIds);

    Result<Void> resetPwd(PwdResetRequest request);

    Result<Void> updateStatus(StatusUpdateRequest request);

    void export(HttpServletResponse response, AdminMngQueryRequest request);

}
