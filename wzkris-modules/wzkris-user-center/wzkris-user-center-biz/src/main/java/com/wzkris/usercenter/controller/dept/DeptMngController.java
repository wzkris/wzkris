package com.wzkris.usercenter.controller.dept;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.core.constant.CommonConstants;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateTypeEnum;
import com.wzkris.common.orm.model.BaseController;
import com.wzkris.common.security.annotation.CheckAdminPerms;
import com.wzkris.common.validator.group.ValidationGroups;
import com.wzkris.common.web.utils.BeanUtil;
import com.wzkris.usercenter.domain.DeptInfoDO;
import com.wzkris.usercenter.domain.req.dept.DeptMngQueryReq;
import com.wzkris.usercenter.domain.req.dept.DeptMngReq;
import com.wzkris.usercenter.manager.DeptInfoDscManager;
import com.wzkris.usercenter.mapper.DeptInfoMapper;
import com.wzkris.usercenter.service.DeptInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

/**
 * 部门信息
 *
 * @author wzkris
 */
@Tag(name = "部门管理")
@RestController
@RequestMapping("/dept-manage")
@RequiredArgsConstructor
public class DeptMngController extends BaseController {

    private final DeptInfoMapper deptInfoMapper;

    private final DeptInfoService deptInfoService;

    private final DeptInfoDscManager deptInfoDscManager;

    @Operation(summary = "部门列表(不带分页)")
    @GetMapping("/list")
    @CheckAdminPerms("user-mod:dept-mng:list")
    public Result<List<DeptInfoDO>> list(DeptMngQueryReq queryReq) {
        List<DeptInfoDO> depts = deptInfoDscManager.list(buildQueryWrapper(queryReq));
        return ok(depts);
    }

    private LambdaQueryWrapper<DeptInfoDO> buildQueryWrapper(DeptMngQueryReq queryReq) {
        return new LambdaQueryWrapper<DeptInfoDO>()
                .apply(queryReq.getParentId() != null && queryReq.getParentId() != 0,
                        "{0} = ANY(ancestors)", queryReq.getParentId())
                .and(queryReq.getDeptId() != null && queryReq.getDeptId() != 0,
                        w -> w.eq(DeptInfoDO::getDeptId, queryReq.getDeptId())
                                .or()
                                .apply("{0} = ANY(ancestors)", queryReq.getDeptId())
                )
                .like(StringUtil.isNotEmpty(queryReq.getDeptName()), DeptInfoDO::getDeptName, queryReq.getDeptName())
                .eq(StringUtil.isNotEmpty(queryReq.getStatus()), DeptInfoDO::getStatus, queryReq.getStatus())
                .orderByDesc(DeptInfoDO::getDeptSort, DeptInfoDO::getDeptId);
    }

    @Operation(summary = "根据部门编号获取详细信息")
    @GetMapping("/{deptId}")
    @CheckAdminPerms("user-mod:dept-mng:query")
    public Result<?> getInfo(@PathVariable Long deptId) {
        // 校验权限
        deptInfoDscManager.checkDataScopes(deptId);
        return ok(deptInfoMapper.selectById(deptId));
    }

    @Operation(summary = "新增部门")
    @OperateLog(title = "部门管理", subTitle = "新增部门", type = OperateTypeEnum.INSERT)
    @PostMapping("/add")
    @CheckAdminPerms("user-mod:dept-mng:add")
    public Result<?> add(@Validated @RequestBody DeptMngReq req) {
        // 校验权限
        deptInfoDscManager.checkDataScopes(req.getParentId());
        if (ObjectUtils.isNotEmpty(req.getParentId()) && req.getParentId() != 0) {
            DeptInfoDO info = deptInfoMapper.selectById(req.getParentId());
            // 如果父节点为停用状态,则不允许新增子节点
            if (StringUtil.equals(CommonConstants.STATUS_DISABLE, info.getStatus())) {
                return err40000("无法在被禁用的部门下添加下级");
            }
        }
        return toRes(deptInfoService.saveDept(BeanUtil.convert(req, DeptInfoDO.class)));
    }

    @Operation(summary = "修改部门")
    @OperateLog(title = "部门管理", subTitle = "修改部门", type = OperateTypeEnum.UPDATE)
    @PostMapping("/edit")
    @CheckAdminPerms("user-mod:dept-mng:edit")
    public Result<?> edit(@Validated(value = ValidationGroups.Update.class) @RequestBody DeptMngReq req) {
        // 校验权限
        deptInfoDscManager.checkDataScopes(req.getDeptId());
        if (Objects.equals(req.getParentId(), req.getDeptId())) {
            return err40000("修改部门'" + req.getDeptName() + "'失败，上级部门不能是自己");
        } else if (StringUtil.equals(CommonConstants.STATUS_DISABLE, req.getStatus())
                && deptInfoMapper.existNormalSubDept(req.getDeptId())) {
            return err40000("该部门包含未停用的子部门");
        }
        return toRes(deptInfoService.modifyDept(BeanUtil.convert(req, DeptInfoDO.class)));
    }

    @Operation(summary = "删除部门")
    @OperateLog(title = "部门管理", subTitle = "删除部门", type = OperateTypeEnum.DELETE)
    @PostMapping("/remove")
    @CheckAdminPerms("user-mod:dept-mng:remove")
    public Result<?> remove(@RequestBody Long deptId) {
        deptInfoDscManager.checkDataScopes(deptId);
        if (deptInfoMapper.existSubDept(deptId)) {
            return err40000("存在下级部门,不允许删除");
        }
        if (deptInfoMapper.existAdmin(deptId)) {
            return err40000("部门存在用户,不允许删除");
        }
        return toRes(deptInfoService.removeById(deptId));
    }

}
