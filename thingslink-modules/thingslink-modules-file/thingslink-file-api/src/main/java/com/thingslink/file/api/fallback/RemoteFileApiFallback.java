package com.thingslink.file.api.fallback;

import com.thingslink.common.core.domain.Result;
import com.thingslink.common.core.enums.BizCode;
import com.thingslink.file.api.RemoteFileApi;
import com.thingslink.file.api.domain.SysFile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import static com.thingslink.common.core.domain.Result.resp;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 降级处理
 * @date : 2023/8/4 14:20
 */
@Slf4j
@Component
public class RemoteFileApiFallback implements FallbackFactory<RemoteFileApi> {


    @Override
    public RemoteFileApi create(Throwable cause) {
        log.error("-------openfeign触发熔断，文件服务调用失败-------");
        return new RemoteFileApi() {
            @Override
            public Result<SysFile> upload(MultipartFile file) {
                log.error("上传文件失败，errMsg：{}", cause.getMessage(), cause);
                return resp(BizCode.RPC_INVOCATION, cause.getMessage());
            }
        };
    }
}
