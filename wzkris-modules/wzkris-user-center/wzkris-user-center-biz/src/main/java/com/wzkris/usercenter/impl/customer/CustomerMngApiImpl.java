package com.wzkris.usercenter.impl.customer;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.excel.utils.ExcelUtil;
import com.wzkris.common.orm.model.AbstractApi;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.web.utils.BeanUtil;
import com.wzkris.usercenter.api.customer.CustomerMngApi;
import com.wzkris.usercenter.domain.CustomerInfoDO;
import com.wzkris.usercenter.request.StatusUpdateRequest;
import com.wzkris.usercenter.request.customer.CustomerMngQueryRequest;
import com.wzkris.usercenter.response.customer.CustomerInfoExportResponse;
import com.wzkris.usercenter.response.customer.CustomerMngResponse;
import com.wzkris.usercenter.service.CustomerInfoService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerMngApiImpl extends AbstractApi implements CustomerMngApi {

    private final CustomerInfoService customerInfoService;

    @Override
    public Result<Page<CustomerMngResponse>> queryPage(CustomerMngQueryRequest request) {
        startPage();
        List<CustomerInfoDO> list = customerInfoService.list(this.buildQueryWrapper(request));
        return getDataTable(BeanUtil.convert(list, CustomerMngResponse.class));
    }

    private LambdaQueryWrapper<CustomerInfoDO> buildQueryWrapper(CustomerMngQueryRequest request) {
        return new LambdaQueryWrapper<CustomerInfoDO>()
                .eq(StringUtil.isNotBlank(request.getStatus()), CustomerInfoDO::getStatus, request.getStatus())
                .like(StringUtil.isNotBlank(request.getNickname()), CustomerInfoDO::getNickname, request.getNickname())
                .like(StringUtil.isNotBlank(request.getPhoneNumber()), CustomerInfoDO::getPhoneNumber, request.getPhoneNumber())
                .between(request.getBeginTime() != null && request.getEndTime() != null,
                        CustomerInfoDO::getCreateAt,
                        request.getBeginTime(), request.getEndTime())
                .orderByDesc(CustomerInfoDO::getCustomerId);
    }

    @Override
    public Result<CustomerMngResponse> queryInfo(Long customerId) {
        return ok(BeanUtil.convert(customerInfoService.getById(customerId), CustomerMngResponse.class));
    }

    @Override
    public Result<Void> updateStatus(StatusUpdateRequest request) {
        CustomerInfoDO update = new CustomerInfoDO(request.getId());
        update.setStatus(request.getStatus());
        return toRes(customerInfoService.updateById(update));
    }

    @Override
    public void export(HttpServletResponse response, CustomerMngQueryRequest request) {
        List<CustomerInfoDO> list = customerInfoService.list(this.buildQueryWrapper(request));
        List<CustomerInfoExportResponse> convert = BeanUtil.convert(list, CustomerInfoExportResponse.class);
        ExcelUtil.exportExcel(convert, "客户数据", CustomerInfoExportResponse.class, false, response, null);
    }

}
