package com.thingslink.file.api;

import com.thingslink.common.core.domain.Result;
import com.thingslink.file.api.domain.SysFile;
import com.thingslink.file.api.fallback.RemoteFileApiFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : RPC -- 文件服务
 * @date : 2023/3/13 16:24
 */
@FeignClient(value = "thingslink-file", contextId = "RemoteFileApi", fallbackFactory = RemoteFileApiFallback.class)
public interface RemoteFileApi {
    /**
     * 文件上传
     *
     * @param file 文件
     * @return 返回文件信息
     */
    @Deprecated
    @PostMapping("/inner/file/upload")
    Result<SysFile> upload(MultipartFile file);

}
