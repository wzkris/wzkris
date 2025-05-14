package com.wzkris.file.api;

import com.wzkris.common.oss.service.FileServiceContext;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : rpc - 文件rpc调用
 * @date : 2023/3/13 16:26
 */
@Service
@DubboService
@RequiredArgsConstructor
public class RemoteFileApiImpl implements RemoteFileApi {

    private final FileServiceContext fileServiceContext;

}
