package com.wzkris.user.api.fallback;

import com.wzkris.common.core.domain.Result;
import com.wzkris.common.core.enums.BizCode;
import com.wzkris.user.api.RemoteSysUserApi;
import com.wzkris.user.api.domain.request.LoginInfoReq;
import com.wzkris.user.api.domain.request.QueryPermsReq;
import com.wzkris.user.api.domain.response.SysPermissionResp;
import com.wzkris.user.api.domain.response.SysUserResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import static com.wzkris.common.core.domain.Result.resp;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 系统用户API降级
 * @date : 2023/8/21 13:03
 */
@Slf4j
@Component
public class RemoteSysUserApiFallback implements FallbackFactory<RemoteSysUserApi> {

    @Override
    public RemoteSysUserApi create(Throwable cause) {
        log.error("-----------用户服务发生熔断-----------");
        return new RemoteSysUserApi() {
            @Override
            public Result<SysUserResp> getByUsername(String username, String password) {
                log.error("查询系统用户信息发生异常，errMsg：{}", cause.getMessage(), cause);
                return resp(BizCode.RPC_INVOCATION, cause.getMessage());
            }

            @Override
            public Result<SysUserResp> getByPhoneNumber(String phoneNumber) {
                log.error("查询系统用户信息发生异常，errMsg：{}", cause.getMessage(), cause);
                return resp(BizCode.RPC_INVOCATION, cause.getMessage());
            }

            @Override
            public Result<SysPermissionResp> getPermission(QueryPermsReq queryPermsReq) {
                log.error("查询系统用户权限发生异常，errMsg：{}", cause.getMessage(), cause);
                return resp(BizCode.RPC_INVOCATION, cause.getMessage());
            }

            @Override
            public void updateLoginInfo(LoginInfoReq loginInfoReq) {
                log.error("更新用户登录信息发生异常，errMsg：{}", cause.getMessage(), cause);
            }

        };
    }
}
