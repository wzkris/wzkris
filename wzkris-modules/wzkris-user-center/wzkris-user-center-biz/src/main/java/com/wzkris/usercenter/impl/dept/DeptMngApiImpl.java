package com.wzkris.usercenter.impl.dept;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.orm.model.AbstractApi;
import com.wzkris.common.orm.request.IdRequest;
import com.wzkris.common.web.utils.BeanUtil;
import com.wzkris.usercenter.api.dept.DeptMngApi;
import com.wzkris.usercenter.api.dept.request.DeptMngTreeRequest;
import com.wzkris.usercenter.api.dept.request.DeptMngSaveRequest;
import com.wzkris.usercenter.api.dept.request.DeptMngUpdateRequest;
import com.wzkris.usercenter.api.dept.response.DeptMngResponse;
import com.wzkris.usercenter.domain.DeptInfoDO;
import com.wzkris.usercenter.enums.dept.DeptStatusEnum;
import com.wzkris.usercenter.mapper.DeptInfoMapper;
import com.wzkris.usercenter.service.DeptInfoService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class DeptMngApiImpl extends AbstractApi implements DeptMngApi {

    private final DeptInfoMapper deptInfoMapper;

    private final DeptInfoService deptInfoService;

    @Override
    public Result<List<DeptMngResponse>> queryList(DeptMngTreeRequest request) {
        List<DeptInfoDO> depts = deptInfoMapper.selectLists(buildQueryWrapper(request));
        return ok(BeanUtil.convert(depts, DeptMngResponse.class));
    }

    private LambdaQueryWrapper<DeptInfoDO> buildQueryWrapper(DeptMngTreeRequest request) {
        return new LambdaQueryWrapper<DeptInfoDO>()
                .apply(request.getParentId() != null && request.getParentId() != 0,
                        "{0} = ANY(ancestors)", request.getParentId())
                .and(request.getDeptId() != null && request.getDeptId() != 0,
                        w -> w.eq(DeptInfoDO::getDeptId, request.getDeptId())
                                .or()
                                .apply("{0} = ANY(ancestors)", request.getDeptId())
                )
                .like(StringUtil.isNotEmpty(request.getDeptName()), DeptInfoDO::getDeptName, request.getDeptName())
                .eq(request.getStatus() != null, DeptInfoDO::getStatus, request.getStatus())
                .orderByDesc(DeptInfoDO::getDeptSort, DeptInfoDO::getDeptId);
    }

    @Override
    public Result<DeptMngResponse> queryInfo(IdRequest request) {
        Long deptId = request.getId();
        if (!deptInfoMapper.checkDataScopes(deptId)) {
            return accessDenied("数据权限不足");
        }
        return ok(BeanUtil.convert(deptInfoService.getById(deptId), DeptMngResponse.class));
    }

    @Override
    public Result<?> save(DeptMngSaveRequest request) {
        if (!deptInfoMapper.checkDataScopes(request.getParentId())) {
            return accessDenied("数据权限不足");
        }
        if (ObjectUtils.isNotEmpty(request.getParentId()) && request.getParentId() != 0) {
            DeptInfoDO info = deptInfoService.getById(request.getParentId());
            if (DeptStatusEnum.DISABLE == info.getStatus()) {
                return requestFail("无法在被禁用的部门下添加下级");
            }
        }
        DeptInfoDO deptInfoDO = BeanUtil.convert(request, DeptInfoDO.class);
        deptInfoDO.setStatus(request.getStatus());
        return toRes(deptInfoService.saveDept(deptInfoDO));
    }

    @Override
    public Result<?> update(DeptMngUpdateRequest request) {
        if (!deptInfoMapper.checkDataScopes(request.getDeptId())) {
            return accessDenied("数据权限不足");
        }
        if (Objects.equals(request.getParentId(), request.getDeptId())) {
            return requestFail("修改部门'" + request.getDeptName() + "'失败，上级部门不能是自己");
        }
        DeptInfoDO deptInfoDO = BeanUtil.convert(request, DeptInfoDO.class);
        deptInfoDO.setStatus(request.getStatus());
        return toRes(deptInfoService.updateDept(deptInfoDO));
    }

    @Override
    public Result<?> remove(IdRequest request) {
        Long deptId = request.getId();
        if (!deptInfoMapper.checkDataScopes(deptId)) {
            return accessDenied("数据权限不足");
        }
        if (deptInfoService.exists(Wrappers.lambdaQuery(DeptInfoDO.class)
                .eq(DeptInfoDO::getParentId, deptId))) {
            return requestFail("存在下级部门，不允许删除");
        }
        if (deptInfoMapper.existAdmin(deptId)) {
            return requestFail("部门存在用户，不允许删除");
        }
        return toRes(deptInfoService.removeDept(deptId));
    }

}
