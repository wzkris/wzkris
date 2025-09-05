package com.wzkris.user.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.core.domain.Result;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.excel.utils.ExcelUtil;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateType;
import com.wzkris.common.orm.model.BaseController;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.security.annotation.CheckSystemPerms;
import com.wzkris.common.web.utils.BeanUtil;
import com.wzkris.user.domain.CustomerInfoDO;
import com.wzkris.user.domain.export.CustomerInfoExport;
import com.wzkris.user.domain.req.CustomerManageQueryReq;
import com.wzkris.user.domain.req.EditStatusReq;
import com.wzkris.user.mapper.CustomerInfoMapper;
import com.wzkris.user.service.CustomerInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户管理
 *
 * @author wzkris
 */
@Tag(name = "客户管理")
@Validated
@RestController
@RequestMapping("/customer-manage")
@PreAuthorize("@su.isSuperTenant()")
@RequiredArgsConstructor
public class CustomerManageController extends BaseController {

    private final CustomerInfoMapper customerInfoMapper;

    private final CustomerInfoService customerInfoService;

    @Operation(summary = "用户分页列表")
    @GetMapping("/list")
    @CheckSystemPerms("user-mod:customer-mng:list")
    public Result<Page<CustomerInfoDO>> listPage(CustomerManageQueryReq queryReq) {
        startPage();
        List<CustomerInfoDO> list = customerInfoMapper.selectList(this.buildQueryWrapper(queryReq));
        return getDataTable(list);
    }

    private LambdaQueryWrapper<CustomerInfoDO> buildQueryWrapper(CustomerManageQueryReq req) {
        return new LambdaQueryWrapper<CustomerInfoDO>()
                .eq(StringUtil.isNotBlank(req.getStatus()), CustomerInfoDO::getStatus, req.getStatus())
                .like(StringUtil.isNotBlank(req.getNickname()), CustomerInfoDO::getNickname, req.getNickname())
                .like(StringUtil.isNotBlank(req.getPhoneNumber()), CustomerInfoDO::getPhoneNumber, req.getPhoneNumber())
                .between(
                        req.getParams().get("beginTime") != null
                                && req.getParams().get("endTime") != null,
                        CustomerInfoDO::getCreateAt,
                        req.getParams().get("beginTime"),
                        req.getParams().get("endTime"))
                .orderByDesc(CustomerInfoDO::getCustomerId);
    }

    @Operation(summary = "用户详细信息")
    @GetMapping("/{userId}")
    @CheckSystemPerms("user-mod:customer-mng:query")
    public Result<CustomerInfoDO> query(@PathVariable Long userId) {
        return ok(customerInfoMapper.selectById(userId));
    }

    @Operation(summary = "状态修改")
    @OperateLog(title = "系统用户", subTitle = "状态修改", operateType = OperateType.UPDATE)
    @PostMapping("/edit-status")
    @CheckSystemPerms("user-mod:customer-mng:edit")
    public Result<Void> editStatus(@RequestBody EditStatusReq statusReq) {
        // 校验权限
        CustomerInfoDO update = new CustomerInfoDO(statusReq.getId());
        update.setStatus(statusReq.getStatus());
        return toRes(customerInfoMapper.updateById(update));
    }

    @Operation(summary = "导出")
    @OperateLog(title = "用户管理", operateType = OperateType.EXPORT)
    @GetMapping("/export")
    @CheckSystemPerms("user-mod:customer-mng:export")
    public void export(HttpServletResponse response, CustomerManageQueryReq queryReq) {
        List<CustomerInfoDO> list = customerInfoMapper.selectList(this.buildQueryWrapper(queryReq));
        List<CustomerInfoExport> convert = BeanUtil.convert(list, CustomerInfoExport.class);
        ExcelUtil.exportExcel(convert, "用户数据", CustomerInfoExport.class, response);
    }

}
