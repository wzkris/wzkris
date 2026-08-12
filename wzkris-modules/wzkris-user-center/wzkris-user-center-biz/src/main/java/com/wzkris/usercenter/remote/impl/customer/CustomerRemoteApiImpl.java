package com.wzkris.usercenter.remote.impl.customer;

import cn.binarywang.wx.miniapp.api.WxMaService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wzkris.common.core.enums.BizCallCodeEnum;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.usercenter.domain.CustomerInfoDO;
import com.wzkris.usercenter.domain.CustomerSocialInfoDO;
import com.wzkris.usercenter.enums.social.SocialTypeEnum;
import com.wzkris.usercenter.mapper.CustomerSocialInfoMapper;
import com.wzkris.usercenter.remote.api.admin.request.LoginInfoUpdateRequest;
import com.wzkris.usercenter.remote.api.customer.CustomerRemoteApi;
import com.wzkris.usercenter.remote.api.customer.request.CustomerQueryRequest;
import com.wzkris.usercenter.remote.api.customer.request.CustomerSocialUpdateRequest;
import com.wzkris.usercenter.remote.api.customer.request.SocialLoginRequest;
import com.wzkris.usercenter.remote.api.customer.response.CustomerQueryResponse;
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
public class CustomerRemoteApiImpl implements CustomerRemoteApi {

    private final CustomerInfoService customerInfoService;

    private final CustomerSocialInfoMapper customerSocialInfoMapper;

    @Autowired
    @Lazy
    private WxMaService wxMaService;

    @Override
    public Result<List<CustomerQueryResponse>> queryList(CustomerQueryRequest request) {
        LambdaQueryWrapper<CustomerInfoDO> eq = Wrappers.lambdaQuery(CustomerInfoDO.class)
                .eq(StringUtil.isNotBlank(request.getPhoneNumber()), CustomerInfoDO::getPhoneNumber, request.getPhoneNumber());
        List<CustomerInfoDO> list = customerInfoService.list(eq);
        List<CustomerQueryResponse> responseList = new ArrayList<>();
        for (CustomerInfoDO customerInfoDO : list) {
            responseList.add(this.toCustomerQueryResponse(customerInfoDO));
        }
        return Result.ok(responseList);
    }

    @Override
    public Result<CustomerQueryResponse> socialLogin(SocialLoginRequest request) {
        // 渠道分发：当前仅支持微信小程序，socialType 为空兼容旧调用默认小程序
        SocialTypeEnum socialType = StringUtil.isBlank(request.getSocialType())
                ? SocialTypeEnum.WE_XCX
                : SocialTypeEnum.fromValue(request.getSocialType());
        if (socialType != SocialTypeEnum.WE_XCX) {
            return Result.requestFail("暂不支持该渠道类型登录");
        }
        String identifier;
        String phoneNumber = null;
        try {
            identifier = this.getWeXcxOpenid(request.getWxCode(), request.getAppid());
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
        CustomerSocialInfoDO socialInfoDO = customerSocialInfoMapper.selectOne(Wrappers
                .<CustomerSocialInfoDO>lambdaQuery()
                .eq(CustomerSocialInfoDO::getSocialType, SocialTypeEnum.WE_XCX)
                .eq(StringUtil.isNotBlank(request.getAppid()), CustomerSocialInfoDO::getAppid, request.getAppid())
                .eq(CustomerSocialInfoDO::getSocialUid, identifier));
        if (socialInfoDO == null) {
            CustomerInfoDO customerInfoDO = new CustomerInfoDO();
            customerInfoDO.setPhoneNumber(phoneNumber);
            customerInfoDO.setNickname("微信用户" + System.currentTimeMillis());

            CustomerSocialInfoDO customerSocialInfoDO = new CustomerSocialInfoDO();
            customerSocialInfoDO.setSocialUid(identifier);
            customerSocialInfoDO.setSocialType(SocialTypeEnum.WE_XCX);
            customerSocialInfoDO.setAppid(request.getAppid());
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
        CustomerQueryResponse response = this.toCustomerQueryResponse(customerInfoDO);
        response.setSocialUid(identifier);
        return Result.ok(response);
    }

    /**
     * 更新客户社交账号绑定，登录后把当前渠道openid写入social_info，用于支付/渠道识别
     */
    @Override
    public Result<Void> updateSocialInfo(CustomerSocialUpdateRequest request) {
        if (request.getSocialType() != SocialTypeEnum.WE_XCX) {
            return Result.requestFail("暂不支持该渠道类型绑定");
        }
        String identifier;
        try {
            identifier = this.getWeXcxOpenid(request.getWxCode(), request.getAppid());
        } catch (WxErrorException e) {
            log.error("微信小程序绑定openid查询异常", e);
            return Result.requestFail("微信绑定失败");
        }
        if (StringUtil.isBlank(identifier)) {
            return Result.requestFail("微信绑定失败");
        }

        String appid = StringUtil.isBlank(request.getAppid()) ? null : request.getAppid();
        CustomerSocialInfoDO exist = customerSocialInfoMapper.selectOne(Wrappers
                .<CustomerSocialInfoDO>lambdaQuery()
                .eq(CustomerSocialInfoDO::getSocialType, request.getSocialType())
                .eq(CustomerSocialInfoDO::getAppid, appid)
                .eq(CustomerSocialInfoDO::getSocialUid, identifier));
        if (exist != null && !exist.getCustomerId().equals(request.getCustomerId())) {
            return Result.requestFail("该微信已绑定其他账号");
        }
        if (exist == null) {
            CustomerSocialInfoDO socialInfoDO = new CustomerSocialInfoDO();
            socialInfoDO.setCustomerId(request.getCustomerId());
            socialInfoDO.setSocialUid(identifier);
            socialInfoDO.setSocialType(request.getSocialType());
            socialInfoDO.setAppid(appid);
            customerSocialInfoMapper.insert(socialInfoDO);
        }
        return Result.ok();
    }

    /**
     * 按appid切换微信小程序配置换取openid；appid为空则使用默认配置
     */
    private String getWeXcxOpenid(String wxCode, String appid) throws WxErrorException {
        if (StringUtil.isNotBlank(appid)) {
            wxMaService.switchover(appid);
        }
        return wxMaService.getUserService().getSessionInfo(wxCode).getOpenid();
    }

    @Override
    public Result<Void> updateLoginInfo(LoginInfoUpdateRequest request) {
        CustomerInfoDO customerInfoDO = new CustomerInfoDO(request.getId());
        customerInfoDO.setLoginIp(request.getLoginIp());
        customerInfoDO.setLoginDate(request.getLoginDate());
        customerInfoService.updateById(customerInfoDO);
        return Result.ok();
    }

    private CustomerQueryResponse toCustomerQueryResponse(CustomerInfoDO customerInfoDO) {
        if (customerInfoDO == null) {
            return null;
        }
        CustomerQueryResponse response = new CustomerQueryResponse();
        response.setId(customerInfoDO.getId());
        response.setNickname(customerInfoDO.getNickname());
        response.setPhoneNumber(customerInfoDO.getPhoneNumber());
        response.setStatus(customerInfoDO.getStatus());
        return response;
    }

}
