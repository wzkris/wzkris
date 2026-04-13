package com.wzkris.usercenter.controller.dept;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateTypeEnum;
import com.wzkris.common.security.annotation.CheckAdminPerms;
import com.wzkris.usercenter.api.dept.DeptMngApi;
import com.wzkris.usercenter.request.dept.DeptMngQueryRequest;
import com.wzkris.usercenter.request.dept.DeptMngSaveRequest;
import com.wzkris.usercenter.request.dept.DeptMngUpdateRequest;
import com.wzkris.usercenter.response.dept.DeptInfoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "部门管理")
@RestController
@RequestMapping("/dept-manage")
@RequiredArgsConstructor
public class DeptMngController {

    private final DeptMngApi deptMngApi;

    @Operation(summary = "部门列表 (不带分页)")
    @GetMapping("/query-list")
    @CheckAdminPerms("user-mod:dept-mng:list")
    public Result<List<DeptInfoResponse>> queryList(DeptMngQueryRequest request) {
        return deptMngApi.queryList(request);
    }

    @Operation(summary = "根据部门编号获取详细信息")
    @GetMapping("/query-info/{deptId}")
    @CheckAdminPerms("user-mod:dept-mng:query")
    public Result<DeptInfoResponse> queryInfo(@PathVariable Long deptId) {
        return deptMngApi.queryInfo(deptId);
    }

    @Operation(summary = "新增部门")
    @OperateLog(title = "部门管理", subTitle = "新增部门", type = OperateTypeEnum.INSERT)
    @PostMapping("/save")
    @CheckAdminPerms("user-mod:dept-mng:add")
    public Result<?> save(@Validated @RequestBody DeptMngSaveRequest request) {
        return deptMngApi.save(request);
    }

    @Operation(summary = "修改部门")
    @OperateLog(title = "部门管理", subTitle = "修改部门", type = OperateTypeEnum.UPDATE)
    @PostMapping("/update")
    @CheckAdminPerms("user-mod:dept-mng:edit")
    public Result<?> update(@Validated @RequestBody DeptMngUpdateRequest request) {
        return deptMngApi.update(request);
    }

    @Operation(summary = "删除部门")
    @OperateLog(title = "部门管理", subTitle = "删除部门", type = OperateTypeEnum.DELETE)
    @PostMapping("/remove")
    @CheckAdminPerms("user-mod:dept-mng:remove")
    public Result<?> remove(@RequestBody Long deptId) {
        return deptMngApi.remove(deptId);
    }

}

