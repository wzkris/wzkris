package com.wzkris.auth.rmi;

import com.wzkris.common.openfeign.constants.ServiceIdConstant;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = ServiceIdConstant.AUTH, url = "http://whois.pconline.com.cn/ipJson.jsp")
public interface RmiTestFeign {

    @GetMapping
    String getAddress(@RequestParam String ip, @RequestParam boolean json);

}
