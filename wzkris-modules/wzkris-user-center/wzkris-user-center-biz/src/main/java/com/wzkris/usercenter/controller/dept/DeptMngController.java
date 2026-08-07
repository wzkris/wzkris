package com.wzkris.usercenter.controller.dept;

import com.wzkris.common.core.enums.AuthTypeEnum;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateTypeEnum;
import com.wzkris.common.orm.request.IdRequest;
import com.wzkris.common.security.annotation.CheckPerms;
import com.wzkris.usercenter.api.dept.DeptMngApi;
import com.wzkris.usercenter.api.dept.request.DeptMngSaveRequest;
import com.wzkris.usercenter.api.dept.request.DeptMngTreeRequest;
import com.wzkris.usercenter.api.dept.request.DeptMngUpdateRequest;
import com.wzkris.usercenter.api.dept.response.DeptMngQueryResponse;
import com.wzkris.usercenter.api.dept.response.DeptMngListResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "部门管理")
@RestController
@RequestMapping("/dept-manage")
@RequiredArgsConstructor
public class DeptMngController {

    private static final String PERM_PREFIX = "user-mod:dept-mng:";

    private final DeptMngApi deptMngApi;

    @Operation(summary = "部门列表 (不带分页)")
    @GetMapping("/query-list")
    @CheckPerms(checkTypes = AuthTypeEnum.ADMIN, prefix = PERM_PREFIX, value = "list")
    public Result<List<DeptMngListResponse>> queryList(@ParameterObject DeptMngTreeRequest request) {
        return deptMngApi.queryList(request);
    }

    @Operation(summary = "根据部门编号获取详细信息")
    @GetMapping("/query-id/{id}")
    @CheckPerms(checkTypes = AuthTypeEnum.ADMIN, prefix = PERM_PREFIX, value = "query")
    public Result<DeptMngQueryResponse> queryById(@ParameterObject IdRequest request) {
        return deptMngApi.queryById(request);
    }

    @Operation(summary = "新增部门")
    @OperateLog(title = "部门管理", subTitle = "新增部门", type = OperateTypeEnum.INSERT)
    @PostMapping("/save")
    @CheckPerms(checkTypes = AuthTypeEnum.ADMIN, prefix = PERM_PREFIX, value = "add")
    public Result<?> save(@Validated @RequestBody DeptMngSaveRequest request) {
        return deptMngApi.save(request);
    }

    @Operation(summary = "修改部门")
    @OperateLog(title = "部门管理", subTitle = "修改部门", type = OperateTypeEnum.UPDATE)
    @PostMapping("/update")
    @CheckPerms(checkTypes = AuthTypeEnum.ADMIN, prefix = PERM_PREFIX, value = "edit")
    public Result<?> update(@Validated @RequestBody DeptMngUpdateRequest request) {
        return deptMngApi.update(request);
    }

    @Operation(summary = "删除部门")
    @OperateLog(title = "部门管理", subTitle = "删除部门", type = OperateTypeEnum.DELETE)
    @PostMapping("/remove")
    @CheckPerms(checkTypes = AuthTypeEnum.ADMIN, prefix = PERM_PREFIX, value = "remove")
    public Result<?> remove(@RequestBody @Valid IdRequest request) {
        return deptMngApi.remove(request);
    }

}

