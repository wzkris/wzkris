package com.thingslink.user.api.fallback;

import com.thingslink.common.core.domain.Result;
import com.thingslink.common.core.enums.BizCode;
import com.thingslink.user.api.RemoteAppUserApi;
import com.thingslink.user.api.domain.dto.CustomerDTO;
import com.thingslink.user.api.domain.dto.LoginInfoDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import static com.thingslink.common.core.domain.Result.resp;

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
        log.error("-----------用户服务发生熔断-----------");
        return new RemoteAppUserApi() {

            @Override
            public Result<CustomerDTO> getByPhoneNumber(String phoneNumber) {
                log.error("查询app用户信息发生异常，errMsg：{}", cause.getMessage(), cause);
                return resp(BizCode.RPC_INVOCATION, cause.getMessage());
            }

            @Override
            public void updateLoginInfo(LoginInfoDTO loginInfoDTO) {
                log.error("更新app用户登录信息发生异常，errMsg：{}", cause.getMessage(), cause);
            }
        };
    }
}
