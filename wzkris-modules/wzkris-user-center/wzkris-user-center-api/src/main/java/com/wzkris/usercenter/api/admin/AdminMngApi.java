package com.wzkris.usercenter.api.admin;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.orm.request.IdListRequest;
import com.wzkris.common.orm.request.IdRequest;
import com.wzkris.usercenter.request.admin.*;
import com.wzkris.usercenter.request.common.PwdResetRequest;
import com.wzkris.usercenter.request.dept.DeptMngListRequest;
import com.wzkris.usercenter.response.admin.AdminMngResponse;
import com.wzkris.usercenter.response.common.CheckedSelectResponse;
import com.wzkris.usercenter.response.common.SelectTreeResponse;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

public interface AdminMngApi {

    Result<Page<AdminMngResponse>> queryPage(AdminMngPageRequest request);

    Result<List<SelectTreeResponse>> queryDeptSelectTree(DeptMngListRequest request);

    Result<CheckedSelectResponse> queryRoleSelect(AdminMngRoleSelectRequest request);

    Result<AdminMngResponse> queryInfo(IdRequest request);

    Result<Void> save(AdminMngSaveRequest request);

    Result<Void> update(AdminMngUpdateRequest request);

    Result<Void> grantRoles(AdminMngGrantRequest request);

    Result<Void> remove(IdListRequest request);

    Result<Void> resetPwd(PwdResetRequest request);

    void export(HttpServletResponse response, AdminMngPageRequest request);

}
