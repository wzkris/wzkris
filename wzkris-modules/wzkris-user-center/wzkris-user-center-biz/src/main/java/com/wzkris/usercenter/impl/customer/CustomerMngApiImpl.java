package com.wzkris.usercenter.impl.customer;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.BeanCopierUtil;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.excel.utils.ExcelUtil;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.orm.request.IdRequest;
import com.wzkris.common.web.model.AbstractApi;
import com.wzkris.usercenter.api.customer.CustomerMngApi;
import com.wzkris.usercenter.api.customer.request.CustomerMngPageRequest;
import com.wzkris.usercenter.api.customer.response.CustomerMngExportResponse;
import com.wzkris.usercenter.api.customer.response.CustomerMngQueryResponse;
import com.wzkris.usercenter.api.customer.response.CustomerMngPageResponse;
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
    public Result<Page<CustomerMngPageResponse>> queryPage(CustomerMngPageRequest request) {
        IPage<CustomerInfoDO> page = customerInfoService.page(request.buildPage(), this.buildQueryWrapper(request));
        return ok(Page.of(page, BeanCopierUtil.copyList(page.getRecords(), CustomerMngPageResponse.class)));
    }

    private LambdaQueryWrapper<CustomerInfoDO> buildQueryWrapper(CustomerMngPageRequest request) {
        return new LambdaQueryWrapper<CustomerInfoDO>()
                .eq(request.getStatus() != null, CustomerInfoDO::getStatus, request.getStatus())
                .like(StringUtil.isNotBlank(request.getNickname()), CustomerInfoDO::getNickname, request.getNickname())
                .like(StringUtil.isNotBlank(request.getPhoneNumber()), CustomerInfoDO::getPhoneNumber, request.getPhoneNumber())
                .between(request.getBeginTime() != null && request.getEndTime() != null,
                        CustomerInfoDO::getCreateAt,
                        request.getBeginTime(), request.getEndTime())
                .orderByDesc(CustomerInfoDO::getId);
    }

    @Override
    public Result<CustomerMngQueryResponse> queryInfo(IdRequest request) {
        return ok(BeanCopierUtil.copy(customerInfoService.getById(request.getId()), CustomerMngQueryResponse.class));
    }

    @Override
    public void export(HttpServletResponse response, CustomerMngPageRequest request) {
        List<CustomerInfoDO> list = customerInfoService.list(this.buildQueryWrapper(request));
        List<CustomerMngExportResponse> convert = BeanCopierUtil.copyList(list, CustomerMngExportResponse.class);
        ExcelUtil.exportExcel(convert, "客户数据", CustomerMngExportResponse.class, false, response, null);
    }

}
