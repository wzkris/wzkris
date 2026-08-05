package com.wzkris.usercenter.remote.impl.customer;

import cn.binarywang.wx.miniapp.api.WxMaService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wzkris.common.core.enums.BizCallCodeEnum;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.usercenter.domain.CustomerInfoDO;
import com.wzkris.usercenter.domain.CustomerSocialInfoDO;
import com.wzkris.usercenter.enums.social.IdentifierTypeEnum;
import com.wzkris.usercenter.mapper.CustomerSocialInfoMapper;
import com.wzkris.usercenter.remote.api.admin.request.LoginInfoUpdateRequest;
import com.wzkris.usercenter.remote.api.customer.CustomerInfoRemoteApi;
import com.wzkris.usercenter.remote.api.customer.request.CustomerQueryRequest;
import com.wzkris.usercenter.remote.api.customer.request.WexcxLoginRequest;
import com.wzkris.usercenter.remote.api.customer.response.CustomerResponse;
import com.wzkris.usercenter.service.CustomerInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerInfoRemoteApiImpl implements CustomerInfoRemoteApi {

    private final CustomerInfoService customerInfoService;

    private final CustomerSocialInfoMapper customerSocialInfoMapper;

    @Autowired
    @Lazy
    private WxMaService wxMaService;

    @Override
    public Result<List<CustomerResponse>> queryList(CustomerQueryRequest request) {
        LambdaQueryWrapper<CustomerInfoDO> eq = Wrappers.lambdaQuery(CustomerInfoDO.class)
                .eq(StringUtil.isNotBlank(request.getPhoneNumber()), CustomerInfoDO::getPhoneNumber, request.getPhoneNumber());
        List<CustomerInfoDO> list = customerInfoService.list(eq);
        List<CustomerResponse> responseList = new ArrayList<>();
        for (CustomerInfoDO customerInfoDO : list) {
            responseList.add(this.toCustomerResponse(customerInfoDO));
        }
        return Result.ok(responseList);
    }

    @Override
    public Result<CustomerResponse> wexcxLogin(WexcxLoginRequest request) {
        String identifier;
        String phoneNumber = null;
        try {
            identifier = wxMaService
                    .getUserService()
                    .getSessionInfo(request.getWxCode())
                    .getOpenid();
            if (StringUtil.isNotBlank(request.getPhoneCode())) {
                phoneNumber = wxMaService.getUserService().getPhoneNumber(request.getPhoneCode()).getPhoneNumber();
            }
        } catch (WxErrorException e) {
            log.error("微信小程序登录api查询异常", e);
            return Result.init(BizCallCodeEnum.WX_ERROR.value(), null, e.getError().getErrorMsg());
        }

        if (StringUtil.isAnyBlank(identifier)) {
            log.error("微信小程序登录api查询结果为null，登录失败");
            return Result.requestFail("微信小程序登录失败");
        }

        Long customerId;
        CustomerSocialInfoDO socialInfoDO = customerSocialInfoMapper.selectOneByObj(
                CustomerSocialInfoDO::getIdentifier, identifier);
        if (socialInfoDO == null) {
            CustomerInfoDO customerInfoDO = new CustomerInfoDO();
            customerInfoDO.setPhoneNumber(phoneNumber);
            customerInfoDO.setNickname("微信用户" + System.currentTimeMillis());

            CustomerSocialInfoDO customerSocialInfoDO = new CustomerSocialInfoDO();
            customerSocialInfoDO.setIdentifier(identifier);
            customerSocialInfoDO.setIdentifierType(IdentifierTypeEnum.WE_XCX);
            customerId = customerInfoService.registerBySocial(customerInfoDO, customerSocialInfoDO);
        } else {
            customerId = socialInfoDO.getCustomerId();
            if (StringUtil.isNotBlank(phoneNumber)) {
                CustomerInfoDO customerInfoDO = new CustomerInfoDO(customerId);
                customerInfoDO.setPhoneNumber(phoneNumber);
                customerInfoService.updateById(customerInfoDO);
            }
        }
        CustomerInfoDO customerInfoDO = customerInfoService.getById(customerId);
        return Result.ok(this.toCustomerResponse(customerInfoDO));
    }

    @Override
    public Result<Void> updateLoginInfo(LoginInfoUpdateRequest request) {
        CustomerInfoDO customerInfoDO = new CustomerInfoDO(request.getId());
        customerInfoDO.setLoginIp(request.getLoginIp());
        customerInfoDO.setLoginDate(request.getLoginDate());
        customerInfoService.updateById(customerInfoDO);
        return Result.ok();
    }

    private CustomerResponse toCustomerResponse(CustomerInfoDO customerInfoDO) {
        if (customerInfoDO == null) {
            return null;
        }
        CustomerResponse response = new CustomerResponse();
        response.setId(customerInfoDO.getId());
        response.setNickname(customerInfoDO.getNickname());
        response.setPhoneNumber(customerInfoDO.getPhoneNumber());
        response.setStatus(customerInfoDO.getStatus());
        return response;
    }

}

