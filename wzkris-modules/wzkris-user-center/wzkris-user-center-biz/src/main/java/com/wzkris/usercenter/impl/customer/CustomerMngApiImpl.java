package com.wzkris.usercenter.impl.customer;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.excel.utils.ExcelUtil;
import com.wzkris.common.web.model.AbstractApi;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.orm.request.IdRequest;
import com.wzkris.common.web.utils.BeanUtil;
import com.wzkris.usercenter.api.customer.CustomerMngApi;
import com.wzkris.usercenter.api.customer.request.CustomerMngPageRequest;
import com.wzkris.usercenter.api.customer.response.CustomerInfoExportResponse;
import com.wzkris.usercenter.api.customer.response.CustomerMngResponse;
import com.wzkris.usercenter.domain.CustomerInfoDO;
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
    public Result<Page<CustomerMngResponse>> queryPage(CustomerMngPageRequest request) {
        IPage<CustomerInfoDO> page = customerInfoService.page(request.buildPage(), this.buildQueryWrapper(request));
        return ok(Page.of(page, BeanUtil.convert(page.getRecords(), CustomerMngResponse.class)));
    }

    private LambdaQueryWrapper<CustomerInfoDO> buildQueryWrapper(CustomerMngPageRequest request) {
        return new LambdaQueryWrapper<CustomerInfoDO>()
                .eq(request.getStatus() != null, CustomerInfoDO::getStatus, request.getStatus())
                .like(StringUtil.isNotBlank(request.getNickname()), CustomerInfoDO::getNickname, request.getNickname())
                .like(StringUtil.isNotBlank(request.getPhoneNumber()), CustomerInfoDO::getPhoneNumber, request.getPhoneNumber())
                .between(request.getBeginTime() != null && request.getEndTime() != null,
                        CustomerInfoDO::getCreateAt,
                        request.getBeginTime(), request.getEndTime())
                .orderByDesc(CustomerInfoDO::getCustomerId);
    }

    @Override
    public Result<CustomerMngResponse> queryInfo(IdRequest request) {
        return ok(BeanUtil.convert(customerInfoService.getById(request.getId()), CustomerMngResponse.class));
    }

    @Override
    public void export(HttpServletResponse response, CustomerMngPageRequest request) {
        List<CustomerInfoDO> list = customerInfoService.list(this.buildQueryWrapper(request));
        List<CustomerInfoExportResponse> convert = BeanUtil.convert(list, CustomerInfoExportResponse.class);
        ExcelUtil.exportExcel(convert, "客户数据", CustomerInfoExportResponse.class, false, response, null);
    }

}
