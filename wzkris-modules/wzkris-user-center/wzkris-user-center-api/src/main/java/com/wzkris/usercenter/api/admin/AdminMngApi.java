package com.wzkris.usercenter.api.admin;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.orm.request.IdListRequest;
import com.wzkris.common.orm.request.IdRequest;
import com.wzkris.usercenter.api.admin.request.*;
import com.wzkris.usercenter.api.admin.response.AdminMngQueryResponse;
import com.wzkris.usercenter.api.admin.response.AdminMngPageResponse;
import com.wzkris.usercenter.request.PwdResetRequest;
import com.wzkris.usercenter.response.CheckedSelectResponse;
import com.wzkris.usercenter.response.SelectTreeResponse;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

public interface AdminMngApi {

    Result<Page<AdminMngPageResponse>> queryPage(AdminMngPageRequest request);

    Result<List<SelectTreeResponse>> queryDeptSelectTree(AdminMngDeptSelectRequest request);

    Result<CheckedSelectResponse> queryRoleSelect(AdminMngRoleSelectRequest request);

    Result<AdminMngQueryResponse> queryById(IdRequest request);

    Result<Void> save(AdminMngSaveRequest request);

    Result<Void> update(AdminMngUpdateRequest request);

    Result<Void> grantRoles(AdminMngGrantRequest request);

    Result<Void> remove(IdListRequest request);

    Result<Void> resetPwd(PwdResetRequest request);

    void export(HttpServletResponse response, AdminMngPageRequest request);

}
