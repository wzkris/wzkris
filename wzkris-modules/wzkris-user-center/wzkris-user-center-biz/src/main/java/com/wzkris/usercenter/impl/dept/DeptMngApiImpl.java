package com.wzkris.usercenter.impl.dept;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.core.constant.CommonConstants;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.orm.model.AbstractApi;
import com.wzkris.common.web.utils.BeanUtil;
import com.wzkris.usercenter.api.dept.DeptMngApi;
import com.wzkris.usercenter.domain.DeptInfoDO;
import com.wzkris.usercenter.mapper.DeptInfoMapper;
import com.wzkris.usercenter.request.dept.DeptMngQueryRequest;
import com.wzkris.usercenter.request.dept.DeptMngSaveRequest;
import com.wzkris.usercenter.request.dept.DeptMngUpdateRequest;
import com.wzkris.usercenter.response.dept.DeptInfoResponse;
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
    public Result<List<DeptInfoResponse>> queryList(DeptMngQueryRequest request) {
        List<DeptInfoDO> depts = deptInfoMapper.selectLists(buildQueryWrapper(request));
        return ok(BeanUtil.convert(depts, DeptInfoResponse.class));
    }

    private LambdaQueryWrapper<DeptInfoDO> buildQueryWrapper(DeptMngQueryRequest request) {
        return new LambdaQueryWrapper<DeptInfoDO>()
                .apply(request.getParentId() != null && request.getParentId() != 0,
                        "{0} = ANY(ancestors)", request.getParentId())
                .and(request.getDeptId() != null && request.getDeptId() != 0,
                        w -> w.eq(DeptInfoDO::getDeptId, request.getDeptId())
                                .or()
                                .apply("{0} = ANY(ancestors)", request.getDeptId())
                )
                .like(StringUtil.isNotEmpty(request.getDeptName()), DeptInfoDO::getDeptName, request.getDeptName())
                .eq(StringUtil.isNotEmpty(request.getStatus()), DeptInfoDO::getStatus, request.getStatus())
                .orderByDesc(DeptInfoDO::getDeptSort, DeptInfoDO::getDeptId);
    }

    @Override
    public Result<DeptInfoResponse> queryInfo(Long deptId) {
        if (!deptInfoMapper.checkDataScopes(deptId)) {
            return accessDenied("数据权限不足");
        }
        return ok(BeanUtil.convert(deptInfoMapper.selectById(deptId), DeptInfoResponse.class));
    }

    @Override
    public Result<?> save(DeptMngSaveRequest request) {
        if (!deptInfoMapper.checkDataScopes(request.getParentId())) {
            return accessDenied("数据权限不足");
        }
        if (ObjectUtils.isNotEmpty(request.getParentId()) && request.getParentId() != 0) {
            DeptInfoDO info = deptInfoMapper.selectById(request.getParentId());
            if (StringUtil.equals(CommonConstants.STATUS_DISABLE, info.getStatus())) {
                return requestFail("无法在被禁用的部门下添加下级");
            }
        }
        return toRes(deptInfoService.saveDept(BeanUtil.convert(request, DeptInfoDO.class)));
    }

    @Override
    public Result<?> update(DeptMngUpdateRequest request) {
        if (!deptInfoMapper.checkDataScopes(request.getDeptId())) {
            return accessDenied("数据权限不足");
        }
        if (Objects.equals(request.getParentId(), request.getDeptId())) {
            return requestFail("修改部门'" + request.getDeptName() + "'失败，上级部门不能是自己");
        } else if (StringUtil.equals(CommonConstants.STATUS_DISABLE, request.getStatus())
                && deptInfoMapper.existNormalSubDept(request.getDeptId())) {
            return requestFail("该部门包含未停用的子部门");
        }
        return toRes(deptInfoService.updateDept(BeanUtil.convert(request, DeptInfoDO.class)));
    }

    @Override
    public Result<?> remove(Long deptId) {
        if (!deptInfoMapper.checkDataScopes(deptId)) {
            return accessDenied("数据权限不足");
        }
        if (deptInfoMapper.existSubDept(deptId)) {
            return requestFail("存在下级部门，不允许删除");
        }
        if (deptInfoMapper.existAdmin(deptId)) {
            return requestFail("部门存在用户，不允许删除");
        }
        return toRes(deptInfoService.removeDept(deptId));
    }

}
