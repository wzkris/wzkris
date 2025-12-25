package com.wzkris.usercenter.controller.test;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.wzkris.common.core.model.Result;
import jakarta.annotation.security.PermitAll;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description :
 * @date : 2023/12/13 10:46
 */
@RestController
@RequestMapping("/nacos")
@RequiredArgsConstructor
public class NacosTestController {

    private final NamingService namingservice;

    @Value("${spring.application.name}")
    private String serviceName;

    @Value("${spring.cloud.nacos.discovery.group}")
    private String group;

    @PostMapping("test")
    @PermitAll
    public Result result() throws NacosException {
        List<Instance> allInstances = namingservice.getAllInstances(serviceName, group);

        return Result.ok(allInstances);
    }

}
