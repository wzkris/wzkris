package com.thingslink.order;

import com.thingslink.common.security.core.constant.SecurityConstants;
import com.thingslink.common.core.domain.Result;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description :
 * @date : 2023/12/7 16:12
 */
@SpringBootTest
public class SentinelTest {

    @Autowired
    private RestTemplate restTemplate;

    @Test
    public void test() {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add(SecurityConstants.LOGIN_PRINCIPAL, "{}");
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity requestEntity = new HttpEntity(null, requestHeaders);

        for (int i = 0; i < 1000; i++) {
            new Thread(() -> {
                Result result = restTemplate.postForObject("http://localhost:3000/flow_test/test", requestEntity, Result.class);
                System.out.println(result);
            }).start();
        }
    }
}
