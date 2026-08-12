package com.wzkris.auth.remote.interfaces.tenantuser;

import com.wzkris.auth.remote.interfaces.admin.request.LoginInfoUpdateRequest;
import com.wzkris.auth.remote.interfaces.tenantuser.request.TenantUserPermissionQueryRequest;
import com.wzkris.auth.remote.interfaces.tenantuser.request.TenantUserQueryRequest;
import com.wzkris.auth.remote.interfaces.tenantuser.request.TenantUserSocialUpdateRequest;
import com.wzkris.auth.remote.interfaces.tenantuser.request.SocialQueryRequest;
import com.wzkris.auth.remote.interfaces.tenantuser.request.TenantIdRequest;
import com.wzkris.auth.remote.interfaces.tenantuser.response.TenantUserQueryResponse;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.model.UserRole;
import com.wzkris.common.remote.annotation.RemoteInterface;
import com.wzkris.common.remote.constants.ServiceContextPathConstant;
import com.wzkris.common.remote.constants.ServiceIdConstant;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

import java.util.List;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : rpc - 租户用户管理
 * @date : 2024/4/15 16:20
 */
@RemoteInterface(
        serviceId = ServiceIdConstant.USER_CENTER,
        path = ServiceContextPathConstant.USER_CENTER
)
@HttpExchange(url = "/tenant-user-remote")
public interface ITenantUserRemote {

    @PostExchange("/query-list")
    Result<List<TenantUserQueryResponse>> queryList(@RequestBody TenantUserQueryRequest request);

    /**
     * 根据租户ID 查询租户最高管理员用户（用于登录态切换）
     */
    @PostExchange("/query-tenant-administrator")
    Result<TenantUserQueryResponse> queryAdministratorByTenantId(@RequestBody TenantIdRequest request);

    /**
     * 根据社交授权code查询用户
     */
    @PostExchange("/query-by-social")
    Result<TenantUserQueryResponse> queryBySocial(@RequestBody SocialQueryRequest request);

    /**
     * 更新租户用户社交账号绑定，登录后绑定当前渠道openid（用于支付/渠道识别）
     */
    @PostExchange("/update-social-info")
    Result<Void> updateSocialInfo(@RequestBody TenantUserSocialUpdateRequest request);

    /**
     * 查询管理员权限
     */
    @PostExchange("/query-permission")
    Result<List<UserRole>> queryPermission(@RequestBody TenantUserPermissionQueryRequest request);

    /**
     * 更新用户登录信息
     */
    @PostExchange("/update-logininfo")
    Result<Void> updateLoginInfo(@RequestBody LoginInfoUpdateRequest request);

}
