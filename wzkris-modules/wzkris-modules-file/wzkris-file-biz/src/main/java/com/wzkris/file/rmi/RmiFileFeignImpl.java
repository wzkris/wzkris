package com.wzkris.file.rmi;

import com.wzkris.common.oss.service.FileServiceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : rpc - 文件rpc调用
 * @date : 2023/3/13 16:26
 */
@RestController
@RequiredArgsConstructor
public class RmiFileFeignImpl implements RmiFileFeign {

    private final FileServiceContext fileServiceContext;

}
