package com.wzkris.system;

import com.wzkris.system.domain.SysConfig;
import com.wzkris.system.mapper.SysConfigMapper;
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
    private SysConfigMapper sysConfigMapper;

    @Test
    public void test() {
        SysConfig config = sysConfigMapper.selectById(1L);
        System.out.println(config);
    }
}
