package com.wzkris.system.api.fallback;

import com.wzkris.system.api.RemoteLogApi;
import com.wzkris.system.api.domain.request.LoginLogReq;
import com.wzkris.system.api.domain.request.OperLogReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 降级
 * @date : 2023/8/4 9:02
 */
@Slf4j
@Component
public class RemoteLogApiFallback implements FallbackFactory<RemoteLogApi> {

    @Override
    public RemoteLogApi create(Throwable cause) {
        return new RemoteLogApi() {
            @Override
            public void insertOperlog(OperLogReq operLogReq) {
                log.error("插入操作日志发生异常，errMsg：{}", cause.getMessage(), cause);
            }

            @Override
            public void insertLoginlog(LoginLogReq loginLogReq) {
                log.error("插入登录日志发生异常，errMsg：{}", cause.getMessage(), cause);
            }
        };
    }

}
