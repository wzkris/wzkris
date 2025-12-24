package com.wzkris.principal.controller.customer;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.excel.utils.ExcelUtil;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateTypeEnum;
import com.wzkris.common.orm.model.BaseController;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.security.annotation.CheckAdminPerms;
import com.wzkris.common.web.utils.BeanUtil;
import com.wzkris.principal.domain.CustomerInfoDO;
import com.wzkris.principal.domain.export.customer.CustomerInfoExport;
import com.wzkris.principal.domain.req.EditStatusReq;
import com.wzkris.principal.domain.req.customer.CustomerMngQueryReq;
import com.wzkris.principal.mapper.CustomerInfoMapper;
import com.wzkris.principal.service.CustomerInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 客户管理
 *
 * @author wzkris
 */
@Tag(name = "客户管理")
@Validated
@RestController
@RequestMapping("/customer-manage")
@RequiredArgsConstructor
public class CustomerMngController extends BaseController {

    private final CustomerInfoMapper customerInfoMapper;

    private final CustomerInfoService customerInfoService;

    @Operation(summary = "客户分页列表")
    @GetMapping("/page")
    @CheckAdminPerms("prin-mod:customer-mng:page")
    public Result<Page<CustomerInfoDO>> page(CustomerMngQueryReq queryReq) {
        startPage();
        List<CustomerInfoDO> list = customerInfoMapper.selectList(this.buildQueryWrapper(queryReq));
        return getDataTable(list);
    }

    private LambdaQueryWrapper<CustomerInfoDO> buildQueryWrapper(CustomerMngQueryReq req) {
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

    @Operation(summary = "客户详细信息")
    @GetMapping("/{customerId}")
    @CheckAdminPerms("prin-mod:customer-mng:query")
    public Result<CustomerInfoDO> query(@PathVariable Long customerId) {
        return ok(customerInfoMapper.selectById(customerId));
    }

    @Operation(summary = "状态修改")
    @OperateLog(title = "客户管理", subTitle = "状态修改", type = OperateTypeEnum.UPDATE)
    @PostMapping("/edit-status")
    @CheckAdminPerms("prin-mod:customer-mng:edit")
    public Result<Void> editStatus(@RequestBody EditStatusReq statusReq) {
        // 校验权限
        CustomerInfoDO update = new CustomerInfoDO(statusReq.getId());
        update.setStatus(statusReq.getStatus());
        return toRes(customerInfoMapper.updateById(update));
    }

    @Operation(summary = "导出")
    @OperateLog(title = "客户管理", type = OperateTypeEnum.EXPORT)
    @GetMapping("/export")
    @CheckAdminPerms("prin-mod:customer-mng:export")
    public void export(HttpServletResponse response, CustomerMngQueryReq queryReq) {
        List<CustomerInfoDO> list = customerInfoMapper.selectList(this.buildQueryWrapper(queryReq));
        List<CustomerInfoExport> convert = BeanUtil.convert(list, CustomerInfoExport.class);
        ExcelUtil.exportExcel(convert, "客户数据", CustomerInfoExport.class, response);
    }

}
