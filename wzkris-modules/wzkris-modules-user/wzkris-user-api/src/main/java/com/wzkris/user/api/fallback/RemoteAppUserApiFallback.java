package com.wzkris.user.api.fallback;

import com.wzkris.common.core.domain.Result;
import com.wzkris.common.openfeign.utils.FeignErrorUtil;
import com.wzkris.user.api.RemoteAppUserApi;
import com.wzkris.user.api.domain.request.LoginInfoReq;
import com.wzkris.user.api.domain.response.AppUserResp;
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
public class RemoteAppUserApiFallback implements FallbackFactory<RemoteAppUserApi> {

    @Override
    public RemoteAppUserApi create(Throwable cause) {
        return new RemoteAppUserApi() {

            @Override
            public Result<AppUserResp> getByPhoneNumber(String phoneNumber) {
                log.error("查询app用户信息发生异常，errMsg：{}", cause.getMessage(), cause);
                return resp(FeignErrorUtil.getCode(cause), null, cause.getMessage());
            }

            @Override
            public Result<AppUserResp> getOrRegisterByIdentifier(String identifierType, String authCode) {
                log.error("查询app用户信息发生异常，errMsg：{}", cause.getMessage(), cause);
                return resp(FeignErrorUtil.getCode(cause), null, cause.getMessage());
            }

            @Override
            public void updateLoginInfo(LoginInfoReq loginInfoReq) {
                log.error("更新app用户登录信息发生异常，errMsg：{}", cause.getMessage(), cause);
            }
        };
    }
}
