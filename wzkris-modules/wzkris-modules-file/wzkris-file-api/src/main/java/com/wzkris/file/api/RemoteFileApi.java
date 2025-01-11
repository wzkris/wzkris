package com.wzkris.file.api;

import com.wzkris.common.core.constant.ApplicationNameConstants;
import com.wzkris.common.core.domain.Result;
import com.wzkris.file.api.domain.request.SysFileUploadReq;
import com.wzkris.file.api.fallback.RemoteFileApiFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import static com.wzkris.common.core.constant.SecurityConstants.INNER_REQUEST_PATH;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : RPC -- 文件服务
 * @date : 2023/3/13 16:24
 */
@FeignClient(value = ApplicationNameConstants.FILE, contextId = "RemoteFileApi", fallbackFactory = RemoteFileApiFallback.class)
public interface RemoteFileApi {

    /**
     * 文件上传
     *
     * @param file 文件
     * @return 返回文件信息
     */
    @Deprecated
    @PostMapping(INNER_REQUEST_PATH + "/file/upload")
    Result<SysFileUploadReq> upload(MultipartFile file);

}