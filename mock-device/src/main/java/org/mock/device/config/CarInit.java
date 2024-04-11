package org.mock.device.config;

import org.mock.device.util.MqttUtil;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 汽车初始化
 * @date : 2023/12/4 14:08
 */
@Component
public class CarInit implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        MqttUtil.init();
    }
}
