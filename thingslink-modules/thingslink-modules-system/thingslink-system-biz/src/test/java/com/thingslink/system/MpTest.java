package com.thingslink.system;

import com.thingslink.system.domain.Config;
import com.thingslink.system.mapper.ConfigMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description :
 * @date : 2024/1/10 15:04
 */
@SpringBootTest
public class MpTest {
    @Autowired
    private ConfigMapper configMapper;

    @Test
    public void test() {
        Config config = configMapper.selectById(1L);
        System.out.println(config);
    }
}
