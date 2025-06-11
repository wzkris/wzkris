package com.wzkris.user.rmi;

import com.wzkris.common.openfeign.constants.ServiceIdConstant;
import com.wzkris.common.openfeign.core.RmiFeign;
import com.wzkris.user.rmi.domain.req.LoginInfoReq;
import com.wzkris.user.rmi.domain.resp.AppUserResp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : rpc - APP用户
 * @date : 2024/4/15 16:20
 */
@FeignClient(name = ServiceIdConstant.USER, contextId = "RmiAppUserFeign")
public interface RmiAppUserFeign extends RmiFeign {

    String prefix = "/rmi_app_user";

    /**
     * 根据手机号查询app用户
     */
    @PostMapping(prefix + "/get_by_phonenumber")
    AppUserResp getByPhoneNumber(@RequestBody String phoneNumber);

    /**
     * 根据authCode去不同渠道获取三方唯一标识，并转换成用户信息
     */
    @PostMapping(prefix + "/get_or_register_by_identifier")
    AppUserResp getOrRegisterByIdentifier(@RequestParam String identifierType, @RequestParam String authCode);

    /**
     * 更新用户登录信息
     */
    @PostMapping(prefix + "/update_login_info")
    void updateLoginInfo(@RequestBody LoginInfoReq loginInfoReq);

}
