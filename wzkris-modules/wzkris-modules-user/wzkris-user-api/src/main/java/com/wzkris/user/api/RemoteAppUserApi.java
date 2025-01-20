package com.wzkris.user.api;

import com.wzkris.common.core.constant.ApplicationNameConstants;
import com.wzkris.common.core.domain.Result;
import com.wzkris.user.api.domain.request.LoginInfoReq;
import com.wzkris.user.api.domain.response.AppUserResp;
import com.wzkris.user.api.fallback.RemoteAppUserApiFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import static com.wzkris.common.core.constant.SecurityConstants.INNER_NOAUTH_REQUEST_PATH;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : rpc - APP用户
 * @date : 2024/4/15 16:20
 */
@FeignClient(value = ApplicationNameConstants.USER, contextId = "RemoteAppUserApi",
        fallbackFactory = RemoteAppUserApiFallback.class)
public interface RemoteAppUserApi {

    /**
     * 根据手机号查询app用户
     */
    @GetMapping(INNER_NOAUTH_REQUEST_PATH + "/query_app_user_by_phonenumber")
    Result<AppUserResp> getByPhoneNumber(@RequestParam("phoneNumber") String phoneNumber);

    /**
     * 根据authCode去不同渠道获取三方唯一标识，并转换成用户信息
     */
    @GetMapping(INNER_NOAUTH_REQUEST_PATH + "/query_app_user_or_register")
    Result<AppUserResp> getOrRegisterByIdentifier(@RequestParam("identifierType") String identifierType, @RequestParam("authCode") String authCode);

    /**
     * 更新用户登录信息
     */
    @PostMapping(INNER_NOAUTH_REQUEST_PATH + "/update_app_user_logininfo")
    void updateLoginInfo(@RequestBody LoginInfoReq loginInfoReq);
}
