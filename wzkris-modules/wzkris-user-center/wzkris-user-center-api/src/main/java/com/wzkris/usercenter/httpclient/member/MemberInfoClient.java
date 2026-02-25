package com.wzkris.usercenter.httpclient.member;

import com.wzkris.common.httpclient.annotation.HttpClient;
import com.wzkris.common.httpclient.constants.ServiceContextPathConstant;
import com.wzkris.common.httpclient.constants.ServiceIdConstant;
import com.wzkris.usercenter.httpclient.admin.req.LoginInfoReq;
import com.wzkris.usercenter.httpclient.member.fallback.MemberInfoClientFallback;
import com.wzkris.usercenter.httpclient.member.req.QueryMemberPermsReq;
import com.wzkris.usercenter.httpclient.member.resp.MemberInfoResp;
import com.wzkris.usercenter.httpclient.member.resp.MemberPermissionResp;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : rpc - 租户成员管理
 * @date : 2024/4/15 16:20
 */
@HttpClient(
        serviceId = ServiceIdConstant.USER_CENTER,
        path = ServiceContextPathConstant.USER_CENTER,
        fallbackFactory = MemberInfoClientFallback.class
)
@HttpExchange(url = "/member-info-client")
public interface MemberInfoClient {

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
