package com.wzkris.principal.feign.customer;

import com.wzkris.common.web.utils.BeanUtil;
import com.wzkris.principal.domain.CustomerInfoDO;
import com.wzkris.principal.domain.CustomerSocialInfoDO;
import com.wzkris.principal.feign.admin.req.LoginInfoReq;
import com.wzkris.principal.feign.customer.req.SocialLoginReq;
import com.wzkris.principal.feign.customer.resp.CustomerResp;
import com.wzkris.principal.mapper.CustomerInfoMapper;
import com.wzkris.principal.mapper.CustomerSocialInfoMapper;
import com.wzkris.principal.service.CustomerInfoService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Hidden
@RestController
@RequestMapping("/feign-customer")
@RequiredArgsConstructor
public class CustomerInfoFeignImpl implements CustomerInfoFeign {

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
        CustomerInfoDO customerInfoDO = new CustomerInfoDO(loginInfoReq.getId());
        customerInfoDO.setLoginIp(loginInfoReq.getLoginIp());
        customerInfoDO.setLoginDate(loginInfoReq.getLoginDate());

        customerInfoMapper.updateById(customerInfoDO);
    }

}
