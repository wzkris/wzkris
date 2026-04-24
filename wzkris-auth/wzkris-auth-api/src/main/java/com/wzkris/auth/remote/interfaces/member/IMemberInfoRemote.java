package com.wzkris.auth.remote.interfaces.member;

import com.wzkris.auth.remote.interfaces.admin.request.LoginInfoUpdateRequest;
import com.wzkris.auth.remote.interfaces.common.request.StringValueRequest;
import com.wzkris.auth.remote.interfaces.member.request.MemberPermsQueryRequest;
import com.wzkris.auth.remote.interfaces.member.response.MemberInfoResponse;
import com.wzkris.auth.remote.interfaces.member.response.MemberPermissionResponse;
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
@HttpExchange(url = "/member-info-remote")
public interface IMemberInfoRemote {

    /**
     * 根据用户名查询用户
     */
    @PostExchange("/query-by-username")
    Result<MemberInfoResponse> queryByUsername(@RequestBody StringValueRequest request);

    /**
     * 根据手机号查询用户
     */
    @PostExchange("/query-by-phonenumber")
    Result<MemberInfoResponse> queryByPhoneNumber(@RequestBody StringValueRequest request);

    /**
     * 根据微信小程序code查询用户
     */
    @PostExchange("/query-by-wexcxcode")
    Result<MemberInfoResponse> queryByWexcxCode(@RequestBody StringValueRequest request);

    /**
     * 查询管理员权限
     */
    @PostExchange("/query-permission")
    Result<MemberPermissionResponse> queryPermission(@RequestBody MemberPermsQueryRequest memberPermsReq);

    /**
     * 更新用户登录信息
     */
    @PostExchange("/update-logininfo")
    Result<Void> updateLoginInfo(@RequestBody LoginInfoUpdateRequest LoginInfoUpdateRequest);

}

