package com.wzkris.auth.algorithm;

import com.wzkris.auth.rmi.RmiTestFeign;
import com.wzkris.auth.rmi.RmiTokenFeign;
import com.wzkris.common.core.utils.SpringUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Proxy;
import java.util.Map;

@SpringBootTest
public class FeignTest {

    @Autowired
    private RmiTestFeign rmiTestFeign;

    @Test
    public void test() {
        Map<String, RmiTokenFeign> beansOfType = SpringUtil.getContext().getBeansOfType(RmiTokenFeign.class);
        if (beansOfType.size() > 1) {
            for (Map.Entry<String, RmiTokenFeign> feignEntry : beansOfType.entrySet()) {
                if (Proxy.isProxyClass(feignEntry.getValue().getClass())) {
                    System.out.println(feignEntry);
                } else {
                    System.out.println(feignEntry);
                }
            }
        }

        String address =
                rmiTestFeign.getAddress("127.0.0.1", true);
        System.out.println(address);
    }

}
