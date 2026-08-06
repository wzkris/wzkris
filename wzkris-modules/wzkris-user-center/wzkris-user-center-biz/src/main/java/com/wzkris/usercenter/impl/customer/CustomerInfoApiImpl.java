package com.wzkris.usercenter.impl.customer;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.security.utils.SecurityUtil;
import com.wzkris.common.web.model.AbstractApi;
import com.wzkris.usercenter.api.customer.CustomerInfoApi;
import com.wzkris.usercenter.api.customer.request.CustomerInfoBasicUpdateRequest;
import com.wzkris.usercenter.api.customer.response.CustomerInfoQueryResponse;
import com.wzkris.usercenter.domain.CustomerInfoDO;
import com.wzkris.usercenter.service.CustomerInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerInfoApiImpl extends AbstractApi implements CustomerInfoApi {

    private final CustomerInfoService customerInfoService;

    @Override
    public Result<CustomerInfoQueryResponse> queryInfo() {
        CustomerInfoDO customerInfoDO = customerInfoService.getById(SecurityUtil.getUid());
        CustomerInfoQueryResponse customerInfoVO = new CustomerInfoQueryResponse();
        customerInfoVO.setNickname(customerInfoDO.getNickname());
        customerInfoVO.setPhoneNumber(customerInfoDO.getPhoneNumber());
        customerInfoVO.setGender(customerInfoDO.getGender());
        customerInfoVO.setAvatar(customerInfoDO.getAvatar());
        customerInfoVO.setLoginDate(customerInfoDO.getLoginDate());
        return ok(customerInfoVO);
    }

    @Override
    public Result<?> updateBasicInfo(CustomerInfoBasicUpdateRequest request) {
        CustomerInfoDO customerInfoDO = new CustomerInfoDO(SecurityUtil.getUid());
        customerInfoDO.setNickname(request.getNickname());
        customerInfoDO.setGender(request.getGender());
        customerInfoDO.setAvatar(request.getAvatar());
        return toRes(customerInfoService.updateById(customerInfoDO));
    }

}
