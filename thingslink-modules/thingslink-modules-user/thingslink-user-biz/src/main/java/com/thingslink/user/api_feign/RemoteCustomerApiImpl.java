package com.thingslink.user.api_feign;

import com.thingslink.common.core.domain.Result;
import com.thingslink.common.core.utils.MapstructUtil;
import com.thingslink.common.security.annotation.InnerAuth;
import com.thingslink.user.api.RemoteCustomerApi;
import com.thingslink.user.api.domain.dto.CustomerDTO;
import com.thingslink.user.api.domain.dto.LoginInfoDTO;
import com.thingslink.user.domain.Customer;
import com.thingslink.user.mapper.CustomerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import static com.thingslink.common.core.domain.Result.success;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 内部app用户接口
 * @date : 2024/4/15 16:20
 */
@InnerAuth
@RestController
@RequiredArgsConstructor
public class RemoteCustomerApiImpl implements RemoteCustomerApi {
    private final CustomerMapper customerMapper;

    /**
     * 根据手机号查询app用户
     */
    @Override
    public Result<CustomerDTO> getByPhoneNumber(String phoneNumber) {
        Customer customer = customerMapper.selectByPhoneNumber(phoneNumber);
        CustomerDTO customerDTO = MapstructUtil.convert(customer, CustomerDTO.class);
        return success(customerDTO);
    }

    /**
     * 更新用户登录信息
     */
    @Override
    public void updateLoginInfo(LoginInfoDTO loginInfoDTO) {
        Customer customer = new Customer(loginInfoDTO.getUserId());
        customer.setLoginIp(loginInfoDTO.getLoginIp());
        customer.setLoginDate(loginInfoDTO.getLoginDate());

        customerMapper.updateById(customer);
    }
}
