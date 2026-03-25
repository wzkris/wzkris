package com.wzkris.usercenter.remoteimpl.customer;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.web.utils.BeanUtil;
import com.wzkris.usercenter.domain.CustomerInfoDO;
import com.wzkris.usercenter.domain.CustomerSocialInfoDO;
import com.wzkris.usercenter.enums.IdentifierTypeEnum;
import com.wzkris.usercenter.remoteimpl.admin.req.LoginInfoUpdateReq;
import com.wzkris.usercenter.remoteimpl.customer.req.WexcxLoginReq;
import com.wzkris.usercenter.remoteimpl.customer.resp.CustomerResp;
import com.wzkris.usercenter.mapper.CustomerInfoMapper;
import com.wzkris.usercenter.mapper.CustomerSocialInfoMapper;
import com.wzkris.usercenter.service.CustomerInfoService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Hidden
@RestController
@RequestMapping("/customer-info-client")
@RequiredArgsConstructor
public class CustomerInfoClientImpl {

    private final CustomerInfoMapper customerInfoMapper;

    private final CustomerInfoService customerInfoService;

    private final CustomerSocialInfoMapper customerSocialInfoMapper;

    private final TransactionTemplate transactionTemplate;

    @PostMapping("/query-by-phonenumber")
    public Result<CustomerResp> getByPhoneNumber(String phoneNumber) {
        CustomerInfoDO customerInfoDO = customerInfoMapper.selectByPhoneNumber(phoneNumber);
        return Result.ok(BeanUtil.convert(customerInfoDO, CustomerResp.class));
    }

    @PostMapping("/wexcx-login")
    public Result<CustomerResp> wexcxLogin(WexcxLoginReq req) {
        Long customerId;
        CustomerSocialInfoDO thirdinfo = customerSocialInfoMapper.selectByIdentifier(req.getIdentifier());
        if (thirdinfo == null) {
            final CustomerInfoDO customerInfoDO = new CustomerInfoDO();
            customerInfoDO.setPhoneNumber(req.getPhoneNumber());
            customerInfoDO.setNickname("微信用户" + System.currentTimeMillis());

            final CustomerSocialInfoDO socialInfoDO = new CustomerSocialInfoDO();
            socialInfoDO.setIdentifier(req.getIdentifier());
            socialInfoDO.setIdentifierType(IdentifierTypeEnum.WE_XCX.getValue());

            customerId = customerInfoService.registerBySocial(customerInfoDO, socialInfoDO);
        } else {
            // 根据标识更新手机号
            customerId = thirdinfo.getCustomerId();
            if (StringUtil.isNotBlank(req.getPhoneNumber())) {
                CustomerInfoDO customerInfoDO = new CustomerInfoDO(customerId);
                customerInfoDO.setPhoneNumber(req.getPhoneNumber());
                customerInfoMapper.updateById(customerInfoDO);
            }
        }
        CustomerInfoDO customerInfo = customerInfoMapper.selectById(customerId);

        return Result.ok(BeanUtil.convert(customerInfo, CustomerResp.class));
    }

    @PostMapping("/update-logininfo")
    public Result<Void> updateLoginInfo(LoginInfoUpdateReq loginInfoUpdateReq) {
        CustomerInfoDO customerInfoDO = new CustomerInfoDO(loginInfoUpdateReq.getId());
        customerInfoDO.setLoginIp(loginInfoUpdateReq.getLoginIp());
        customerInfoDO.setLoginDate(loginInfoUpdateReq.getLoginDate());

        customerInfoMapper.updateById(customerInfoDO);
        return Result.ok();
    }

}
