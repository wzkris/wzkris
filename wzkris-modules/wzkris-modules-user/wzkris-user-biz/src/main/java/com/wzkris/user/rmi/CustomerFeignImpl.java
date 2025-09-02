package com.wzkris.user.rmi;

import cn.binarywang.wx.miniapp.api.WxMaService;
import com.wzkris.common.core.enums.BizCallCode;
import com.wzkris.common.core.exception.service.ExternalServiceException;
import com.wzkris.common.web.utils.BeanUtil;
import com.wzkris.user.domain.CustomerInfoDO;
import com.wzkris.user.domain.CustomerSocialInfoDO;
import com.wzkris.user.mapper.CustomerInfoMapper;
import com.wzkris.user.mapper.CustomerSocialInfoMapper;
import com.wzkris.user.rmi.domain.req.LoginInfoReq;
import com.wzkris.user.rmi.domain.resp.CustomerResp;
import com.wzkris.user.service.CustomerInfoService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
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

    @Autowired
    @Lazy
    private WxMaService wxMaService;

    @Autowired
    @Lazy
    private WxMpService wxMpService;

    @Override
    public CustomerResp getByPhoneNumber(String phoneNumber) {
        CustomerInfoDO customerInfoDO = customerInfoMapper.selectByPhoneNumber(phoneNumber);
        return BeanUtil.convert(customerInfoDO, CustomerResp.class);
    }

    @Override
    public CustomerResp getOrRegisterByIdentifier(String identifierType, String authCode) {
        CustomerSocialInfoDO.IdentifierType type = CustomerSocialInfoDO.IdentifierType.valueOf(identifierType);

        String identifier;
        try {
            switch (type) {
                case WX_XCX -> {
                    identifier = wxMaService
                            .getUserService()
                            .getSessionInfo(authCode)
                            .getOpenid();
                }
                case WX_GZH -> {
                    identifier = wxMpService
                            .getOAuth2Service()
                            .getAccessToken(authCode)
                            .getOpenId();
                }
                default -> identifier = null;
            }
        } catch (WxErrorException e) {
            throw new ExternalServiceException(BizCallCode.WX_ERROR.value(), e.getError().getErrorMsg());
        }

        CustomerSocialInfoDO userThirdinfo = customerSocialInfoMapper.selectByIdentifier(identifier);
        if (userThirdinfo == null) {
            userThirdinfo = transactionTemplate.execute(status -> {
                CustomerInfoDO customerInfoDO = new CustomerInfoDO();
                customerInfoService.saveCustomer(customerInfoDO);

                CustomerSocialInfoDO userthirdinfo = new CustomerSocialInfoDO();
                userthirdinfo.setCustomerId(customerInfoDO.getCustomerId());
                userthirdinfo.setIdentifier(identifier);
                userthirdinfo.setIdentifierType(CustomerSocialInfoDO.IdentifierType.WX_XCX.getValue());
                customerSocialInfoMapper.insert(userthirdinfo);
                return userthirdinfo;
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
