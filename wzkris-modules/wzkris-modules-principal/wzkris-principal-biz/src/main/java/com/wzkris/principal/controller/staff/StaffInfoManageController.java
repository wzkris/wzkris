package com.wzkris.principal.controller.staff;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.SpringUtil;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateType;
import com.wzkris.common.orm.model.BaseController;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.security.annotation.CheckStaffPerms;
import com.wzkris.common.security.annotation.enums.CheckMode;
import com.wzkris.common.security.utils.StaffUtil;
import com.wzkris.common.validator.group.ValidationGroups;
import com.wzkris.common.web.utils.BeanUtil;
import com.wzkris.principal.domain.StaffInfoDO;
import com.wzkris.principal.domain.req.EditStatusReq;
import com.wzkris.principal.domain.req.ResetPwdReq;
import com.wzkris.principal.domain.req.staff.StaffManageQueryReq;
import com.wzkris.principal.domain.req.staff.StaffManageReq;
import com.wzkris.principal.domain.req.staff.StaffToPostsReq;
import com.wzkris.principal.domain.vo.CheckedSelectVO;
import com.wzkris.principal.domain.vo.staff.StaffManageVO;
import com.wzkris.principal.listener.event.CreateUserEvent;
import com.wzkris.principal.mapper.StaffInfoMapper;
import com.wzkris.principal.service.PostInfoService;
import com.wzkris.principal.service.StaffInfoService;
import com.wzkris.principal.service.TenantInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

/**
 * 员工管理
 *
 * @author wzkris
 */
@Tag(name = "租户员工管理")
@Validated
@RestController
@RequestMapping("/staff-manage")
@RequiredArgsConstructor
public class StaffInfoManageController extends BaseController {

    private final StaffInfoMapper staffInfoMapper;

    private final StaffInfoService staffInfoService;

    private final PostInfoService postInfoService;

    private final TenantInfoService tenantInfoService;

    private final PasswordEncoder passwordEncoder;

    @Operation(summary = "分页列表")
    @GetMapping("/list")
    @CheckStaffPerms("prin-mod:staff-mng:list")
    public Result<Page<StaffManageVO>> listPage(StaffManageQueryReq queryReq) {
        startPage();
        List<StaffManageVO> list = staffInfoMapper.listVO(this.buildPageWrapper(queryReq));
        return getDataTable(list);
    }

    private QueryWrapper<StaffInfoDO> buildPageWrapper(StaffManageQueryReq queryReq) {
        return new QueryWrapper<StaffInfoDO>()
                .like(ObjectUtils.isNotEmpty(queryReq.getUsername()), "username", queryReq.getUsername())
                .like(ObjectUtils.isNotEmpty(queryReq.getPhoneNumber()), "phone_number", queryReq.getPhoneNumber())
                .eq(ObjectUtils.isNotEmpty(queryReq.getStatus()), "s.status", queryReq.getStatus())
                .between(queryReq.getParam("beginTime") != null && queryReq.getParam("endTime") != null,
                        "s.create_at",
                        queryReq.getParam("beginTime"),
                        queryReq.getParam("endTime"));
    }

    @Operation(summary = "员工详细信息")
    @GetMapping("/{staffId}")
    @CheckStaffPerms("prin-mod:staff-mng:list")
    public Result<StaffInfoDO> getInfo(@PathVariable Long staffId) {
        tenantInfoService.checkAdministrator(staffId);
        return ok(staffInfoMapper.selectById(staffId));
    }

    @Operation(summary = "员工-职位选择列表")
    @GetMapping({"/post-checked-select/", "/post-checked-select/{staffId}"})
    @CheckStaffPerms(
            value = {"prin-mod:staff-mng:edit", "prin-mod:staff-mng:add"},
            mode = CheckMode.OR)
    public Result<CheckedSelectVO> postSelect(@PathVariable(required = false) Long staffId, String postName) {
        CheckedSelectVO checkedSelectVO = new CheckedSelectVO();
        checkedSelectVO.setCheckedKeys(staffId == null ? Collections.emptyList() : postInfoService.listIdByStaffId(staffId));
        checkedSelectVO.setSelects(postInfoService.listSelect(postName));
        return ok(checkedSelectVO);
    }

    @Operation(summary = "新增员工")
    @OperateLog(title = "员工管理", subTitle = "新增员工", operateType = OperateType.INSERT)
    @PostMapping("/add")
    @CheckStaffPerms("prin-mod:staff-mng:add")
    public Result<Void> add(@Validated(ValidationGroups.Insert.class) @RequestBody StaffManageReq staffReq) {
        if (!tenantInfoService.checkAccountLimit(StaffUtil.getTenantId())) {
            return err40000("账号数量已达上限，请联系管理员");
        } else if (staffInfoService.existByStaffName(staffReq.getStaffId(), staffReq.getUsername())) {
            return err40000("添加员工'" + staffReq.getUsername() + "'失败，登录账号已存在");
        } else if (StringUtil.isNotEmpty(staffReq.getPhoneNumber())
                && staffInfoService.existByPhoneNumber(staffReq.getStaffId(), staffReq.getPhoneNumber())) {
            return err40000("添加员工'" + staffReq.getUsername() + "'失败，手机号码已存在");
        }
        StaffInfoDO staff = BeanUtil.convert(staffReq, StaffInfoDO.class);
        String password = RandomStringUtils.secure().nextAlphabetic(8);
        staff.setPassword(password);

        boolean success = staffInfoService.saveStaff(staff, staffReq.getPostIds());
        if (success) {
            SpringUtil.getContext()
                    .publishEvent(new CreateUserEvent(StaffUtil.getId(), staffReq.getUsername(), password));
        }
        return toRes(success);
    }

    @Operation(summary = "修改员工")
    @OperateLog(title = "员工管理", subTitle = "修改员工", operateType = OperateType.UPDATE)
    @PostMapping("/edit")
    @CheckStaffPerms("prin-mod:staff-mng:edit")
    public Result<Void> edit(@Validated @RequestBody StaffManageReq staffReq) {
        tenantInfoService.checkAdministrator(staffReq.getStaffId());
        if (staffInfoService.existByStaffName(staffReq.getStaffId(), staffReq.getUsername())) {
            return err40000("修改员工'" + staffReq.getUsername() + "'失败，登录账号已存在");
        } else if (StringUtil.isNotEmpty(staffReq.getPhoneNumber())
                && staffInfoService.existByPhoneNumber(staffReq.getStaffId(), staffReq.getPhoneNumber())) {
            return err40000("修改员工'" + staffReq.getUsername() + "'失败，手机号码已存在");
        }
        StaffInfoDO staff = BeanUtil.convert(staffReq, StaffInfoDO.class);

        return toRes(staffInfoService.modifyStaff(staff, staffReq.getPostIds()));
    }

    @Operation(summary = "重置密码")
    @OperateLog(title = "员工管理", subTitle = "重置密码", operateType = OperateType.UPDATE)
    @PostMapping("/reset-password")
    @CheckStaffPerms("prin-mod:staff-mng:edit")
    public Result<Void> resetPwd(@RequestBody @Valid ResetPwdReq req) {
        tenantInfoService.checkAdministrator(req.getId());
        StaffInfoDO update = new StaffInfoDO(req.getId());
        update.setPassword(passwordEncoder.encode(req.getPassword()));
        return toRes(staffInfoMapper.updateById(update));
    }

    @Operation(summary = "状态修改")
    @OperateLog(title = "员工管理", subTitle = "状态修改", operateType = OperateType.UPDATE)
    @PostMapping("/edit-status")
    @CheckStaffPerms("prin-mod:staff-mng:edit")
    public Result<Void> editStatus(@RequestBody EditStatusReq statusReq) {
        tenantInfoService.checkAdministrator(statusReq.getId());
        StaffInfoDO update = new StaffInfoDO(statusReq.getId());
        update.setStatus(statusReq.getStatus());
        return toRes(staffInfoMapper.updateById(update));
    }

    @Operation(summary = "授权职位")
    @OperateLog(title = "员工管理", subTitle = "授权员工职位", operateType = OperateType.GRANT)
    @PostMapping("/grant-post")
    @CheckStaffPerms("prin-mod:staff-mng:grant-post")
    public Result<Void> grantPosts(@RequestBody @Valid StaffToPostsReq req) {
        tenantInfoService.checkAdministrator(req.getStaffId());
        return toRes(staffInfoService.grantPosts(req.getStaffId(), req.getPostIds()));
    }

    @Operation(summary = "删除员工")
    @OperateLog(title = "员工管理", subTitle = "删除员工", operateType = OperateType.DELETE)
    @PostMapping("/remove")
    @CheckStaffPerms("prin-mod:staff-mng:remove")
    public Result<Void> remove(@RequestBody List<Long> staffIds) {
        tenantInfoService.checkAdministrator(staffIds);
        return toRes(staffInfoService.removeByIds(staffIds));
    }

}
