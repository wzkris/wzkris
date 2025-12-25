package com.wzkris.usercenter.httpservice.member;

import com.wzkris.common.httpservice.annotation.HttpServiceClient;
import com.wzkris.common.httpservice.constants.ServiceIdConstant;
import com.wzkris.usercenter.httpservice.admin.req.LoginInfoReq;
import com.wzkris.usercenter.httpservice.member.fallback.MemberInfoHttpServiceFallback;
import com.wzkris.usercenter.httpservice.member.req.QueryMemberPermsReq;
import com.wzkris.usercenter.httpservice.member.resp.MemberInfoResp;
import com.wzkris.usercenter.httpservice.member.resp.MemberPermissionResp;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : rpc - 租户成员管理
 * @date : 2024/4/15 16:20
 */
@HttpServiceClient(
        serviceId = ServiceIdConstant.USER_CENTER,
        fallbackFactory = MemberInfoHttpServiceFallback.class
)
@HttpExchange(url = "/feign-member")
public interface MemberInfoHttpService {

    /**
     * 根据用户名查询用户
     */
    @PostExchange("/query-by-username")
    MemberInfoResp getByUsername(@RequestBody String username);

    /**
     * 根据手机号查询用户
     */
    @PostExchange("/query-by-phonenumber")
    MemberInfoResp getByPhoneNumber(@RequestBody String phoneNumber);

    /**
     * 根据微信小程序code查询用户
     */
    @PostExchange("/query-by-wexcx-identifier")
    MemberInfoResp getByWexcxIdentifier(@RequestBody String xcxIdentifier);

    /**
     * 查询管理员权限
     */
    @PostExchange("/query-permission")
    MemberPermissionResp getPermission(@RequestBody QueryMemberPermsReq memberPermsReq);

    /**
     * 更新用户登录信息
     */
    @PostExchange("/update-logininfo")
    void updateLoginInfo(@RequestBody LoginInfoReq loginInfoReq);

}
