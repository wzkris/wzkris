package com.wzkris.user.rmi;

import com.wzkris.user.rmi.domain.req.LoginInfoReq;
import com.wzkris.user.rmi.domain.resp.AppUserResp;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : rpc - APP用户
 * @date : 2024/4/15 16:20
 */
public interface RmiAppUserService {

    /**
     * 根据手机号查询app用户
     */
    AppUserResp getByPhoneNumber(String phoneNumber);

    /**
     * 根据authCode去不同渠道获取三方唯一标识，并转换成用户信息
     */
    AppUserResp getOrRegisterByIdentifier(String identifierType, String authCode);

    /**
     * 更新用户登录信息
     */
    void updateLoginInfo(LoginInfoReq loginInfoReq);
}
