package com.wzkris.usercenter.httpclientimpl.customer;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.web.utils.BeanUtil;
import com.wzkris.usercenter.domain.CustomerInfoDO;
import com.wzkris.usercenter.domain.CustomerSocialInfoDO;
import com.wzkris.usercenter.enums.IdentifierTypeEnum;
import com.wzkris.usercenter.httpclient.admin.req.LoginInfoUpdateReq;
import com.wzkris.usercenter.httpclient.customer.CustomerInfoClient;
import com.wzkris.usercenter.httpclient.customer.req.WexcxLoginReq;
import com.wzkris.usercenter.httpclient.customer.resp.CustomerResp;
import com.wzkris.usercenter.mapper.CustomerInfoMapper;
import com.wzkris.usercenter.mapper.CustomerSocialInfoMapper;
import com.wzkris.usercenter.service.CustomerInfoService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.RestController;

@Hidden
@RestController
@RequiredArgsConstructor
public class CustomerInfoClientImpl implements CustomerInfoClient {

    private final CustomerInfoMapper customerInfoMapper;

    private final CustomerInfoService customerInfoService;

    private final CustomerSocialInfoMapper customerSocialInfoMapper;

    private final TransactionTemplate transactionTemplate;

    @Override
    public Result<CustomerResp> getByPhoneNumber(String phoneNumber) {
        CustomerInfoDO customerInfoDO = customerInfoMapper.selectByPhoneNumber(phoneNumber);
        return Result.ok(BeanUtil.convert(customerInfoDO, CustomerResp.class));
    }

    @Override
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

    @Override
    public Result<Void> updateLoginInfo(LoginInfoUpdateReq loginInfoUpdateReq) {
        CustomerInfoDO customerInfoDO = new CustomerInfoDO(loginInfoUpdateReq.getId());
        customerInfoDO.setLoginIp(loginInfoUpdateReq.getLoginIp());
        customerInfoDO.setLoginDate(loginInfoUpdateReq.getLoginDate());

        customerInfoMapper.updateById(customerInfoDO);
        return Result.ok();
    }

}
