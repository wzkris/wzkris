package com.wzkris.file.rmi;

import com.wzkris.common.oss.service.FileServiceContext;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@Hidden
@RestController
@RequiredArgsConstructor
public class RmiSysFileFeignImpl implements RmiSysFileFeign {

    private final FileServiceContext fileServiceContext;

}
