package com.wzkris.auth.remote.member;

import com.wzkris.auth.remote.admin.req.LoginInfoUpdateReq;
import com.wzkris.auth.remote.member.req.MemberPermsQueryReq;
import com.wzkris.auth.remote.member.resp.MemberInfoResp;
import com.wzkris.auth.remote.member.resp.MemberPermissionResp;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.remote.annotation.RemoteInterface;
import com.wzkris.common.remote.constants.ServiceContextPathConstant;
import com.wzkris.common.remote.constants.ServiceIdConstant;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : rpc - 租户成员管理
 * @date : 2024/4/15 16:20
 */
@RemoteInterface(
        serviceId = ServiceIdConstant.USER_CENTER,
        path = ServiceContextPathConstant.USER_CENTER
)
@HttpExchange(url = "/member-info-client")
public interface IMemberInfoRemote {

    /**
     * 根据用户名查询用户
     */
    @PostExchange("/query-by-username")
    Result<MemberInfoResp> getByUsername(@RequestBody String username);

    /**
     * 根据手机号查询用户
     */
    @PostExchange("/query-by-phonenumber")
    Result<MemberInfoResp> getByPhoneNumber(@RequestBody String phoneNumber);

    /**
     * 根据微信小程序code查询用户
     */
    @PostExchange("/query-by-wexcx-identifier")
    Result<MemberInfoResp> getByWexcxIdentifier(@RequestBody String xcxIdentifier);

    /**
     * 查询管理员权限
     */
    @PostExchange("/query-permission")
    Result<MemberPermissionResp> getPermission(@RequestBody MemberPermsQueryReq memberPermsReq);

    /**
     * 更新用户登录信息
     */
    @PostExchange("/update-logininfo")
    Result<Void> updateLoginInfo(@RequestBody LoginInfoUpdateReq loginInfoUpdateReq);

}
