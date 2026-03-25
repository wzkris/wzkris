package com.wzkris.usercenter.remote.impl.customer;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.usercenter.domain.CustomerInfoDO;
import com.wzkris.usercenter.domain.CustomerSocialInfoDO;
import com.wzkris.usercenter.enums.IdentifierTypeEnum;
import com.wzkris.usercenter.mapper.CustomerInfoMapper;
import com.wzkris.usercenter.mapper.CustomerSocialInfoMapper;
import com.wzkris.usercenter.remote.api.admin.request.LoginInfoUpdateRequest;
import com.wzkris.usercenter.remote.api.customer.CustomerInfoRemoteApi;
import com.wzkris.usercenter.remote.api.customer.request.WexcxLoginRequest;
import com.wzkris.usercenter.remote.api.customer.response.CustomerResponse;
import com.wzkris.usercenter.service.CustomerInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerInfoRemoteApiImpl implements CustomerInfoRemoteApi {

    private final CustomerInfoMapper customerInfoMapper;

    private final CustomerInfoService customerInfoService;

    private final CustomerSocialInfoMapper customerSocialInfoMapper;

    @Override
    public Result<CustomerResponse> getByPhoneNumber(String phoneNumber) {
        CustomerInfoDO customerInfoDO = customerInfoMapper.selectByPhoneNumber(phoneNumber);
        return Result.ok(this.toCustomerResponse(customerInfoDO));
    }

    @Override
    public Result<CustomerResponse> wexcxLogin(WexcxLoginRequest request) {
        Long customerId;
        CustomerSocialInfoDO socialInfoDO = customerSocialInfoMapper.selectByIdentifier(request.getIdentifier());
        if (socialInfoDO == null) {
            CustomerInfoDO customerInfoDO = new CustomerInfoDO();
            customerInfoDO.setPhoneNumber(request.getPhoneNumber());
            customerInfoDO.setNickname("微信用户" + System.currentTimeMillis());

            CustomerSocialInfoDO customerSocialInfoDO = new CustomerSocialInfoDO();
            customerSocialInfoDO.setIdentifier(request.getIdentifier());
            customerSocialInfoDO.setIdentifierType(IdentifierTypeEnum.WE_XCX.getValue());
            customerId = customerInfoService.registerBySocial(customerInfoDO, customerSocialInfoDO);
        } else {
            customerId = socialInfoDO.getCustomerId();
            if (StringUtil.isNotBlank(request.getPhoneNumber())) {
                CustomerInfoDO customerInfoDO = new CustomerInfoDO(customerId);
                customerInfoDO.setPhoneNumber(request.getPhoneNumber());
                customerInfoMapper.updateById(customerInfoDO);
            }
        }
        CustomerInfoDO customerInfoDO = customerInfoMapper.selectById(customerId);
        return Result.ok(this.toCustomerResponse(customerInfoDO));
    }

    @Override
    public Result<Void> updateLoginInfo(LoginInfoUpdateRequest loginInfoUpdateRequest) {
        CustomerInfoDO customerInfoDO = new CustomerInfoDO(loginInfoUpdateRequest.getId());
        customerInfoDO.setLoginIp(loginInfoUpdateRequest.getLoginIp());
        customerInfoDO.setLoginDate(loginInfoUpdateRequest.getLoginDate());
        customerInfoMapper.updateById(customerInfoDO);
        return Result.ok();
    }

    private CustomerResponse toCustomerResponse(CustomerInfoDO customerInfoDO) {
        if (customerInfoDO == null) {
            return null;
        }
        CustomerResponse response = new CustomerResponse();
        response.setCustomerId(customerInfoDO.getCustomerId());
        response.setNickname(customerInfoDO.getNickname());
        response.setPhoneNumber(customerInfoDO.getPhoneNumber());
        response.setStatus(customerInfoDO.getStatus());
        return response;
    }

}

