package com.wzkris.principal.feign.customer;

import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.web.utils.BeanUtil;
import com.wzkris.principal.domain.CustomerInfoDO;
import com.wzkris.principal.domain.CustomerSocialInfoDO;
import com.wzkris.principal.enums.IdentifierType;
import com.wzkris.principal.feign.admin.req.LoginInfoReq;
import com.wzkris.principal.feign.customer.req.WexcxLoginReq;
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
    public CustomerResp wexcxLogin(WexcxLoginReq req) {
        Long customerId;
        CustomerSocialInfoDO thirdinfo = customerSocialInfoMapper.selectByIdentifier(req.getIdentifier());
        if (thirdinfo == null) {
            final CustomerInfoDO customerInfoDO = new CustomerInfoDO();
            customerInfoDO.setPhoneNumber(req.getPhoneNumber());
            customerInfoDO.setNickname("微信用户" + System.currentTimeMillis());

            final CustomerSocialInfoDO socialInfoDO = new CustomerSocialInfoDO();
            socialInfoDO.setIdentifier(req.getIdentifier());
            socialInfoDO.setIdentifierType(IdentifierType.WE_XCX.getValue());

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

        return BeanUtil.convert(customerInfo, CustomerResp.class);
    }

    @Override
    public void updateLoginInfo(LoginInfoReq loginInfoReq) {
        CustomerInfoDO customerInfoDO = new CustomerInfoDO(loginInfoReq.getId());
        customerInfoDO.setLoginIp(loginInfoReq.getLoginIp());
        customerInfoDO.setLoginDate(loginInfoReq.getLoginDate());

        customerInfoMapper.updateById(customerInfoDO);
    }

}
