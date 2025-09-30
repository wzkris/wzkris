package com.wzkris.user.feign.customer;

import com.wzkris.common.web.utils.BeanUtil;
import com.wzkris.user.domain.CustomerInfoDO;
import com.wzkris.user.domain.CustomerSocialInfoDO;
import com.wzkris.user.feign.customer.req.SocialLoginReq;
import com.wzkris.user.feign.customer.resp.CustomerResp;
import com.wzkris.user.feign.userinfo.req.LoginInfoReq;
import com.wzkris.user.mapper.CustomerInfoMapper;
import com.wzkris.user.mapper.CustomerSocialInfoMapper;
import com.wzkris.user.service.CustomerInfoService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Hidden
@RestController
@RequestMapping("/feign-customer")
@RequiredArgsConstructor
public class CustomerFeignImpl implements CustomerFeign {

    private final CustomerInfoMapper customerInfoMapper;

    private final CustomerInfoService customerInfoService;

    private final CustomerSocialInfoMapper customerSocialInfoMapper;

    private final TransactionTemplate transactionTemplate;

    @Override
    public CustomerResp getByPhoneNumber(String phoneNumber) {
        CustomerInfoDO customerInfoDO = customerInfoMapper.selectByPhoneNumber(phoneNumber);
        return BeanUtil.convert(customerInfoDO, CustomerResp.class);
    }

    @Override
    public CustomerResp socialLoginQuery(SocialLoginReq req) {
        CustomerSocialInfoDO userThirdinfo = customerSocialInfoMapper.selectByIdentifier(req.getIdentifier());
        if (userThirdinfo == null) {
            userThirdinfo = transactionTemplate.execute(status -> {
                CustomerInfoDO customerInfoDO = new CustomerInfoDO();
                customerInfoService.saveCustomer(customerInfoDO);

                CustomerSocialInfoDO socialInfoDO = new CustomerSocialInfoDO();
                socialInfoDO.setCustomerId(customerInfoDO.getCustomerId());
                socialInfoDO.setIdentifier(req.getIdentifier());
                socialInfoDO.setIdentifierType(req.getIdentifierType());
                customerSocialInfoMapper.insert(socialInfoDO);
                return socialInfoDO;
            });
        }
        CustomerInfoDO customerInfoDO = customerInfoMapper.selectById(userThirdinfo.getCustomerId());
        return BeanUtil.convert(customerInfoDO, CustomerResp.class);
    }

    @Override
    public void updateLoginInfo(LoginInfoReq loginInfoReq) {
        CustomerInfoDO customerInfoDO = new CustomerInfoDO(loginInfoReq.getUserId());
        customerInfoDO.setLoginIp(loginInfoReq.getLoginIp());
        customerInfoDO.setLoginDate(loginInfoReq.getLoginDate());

        customerInfoMapper.updateById(customerInfoDO);
    }

}
